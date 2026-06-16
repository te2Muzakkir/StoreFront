package com.storefront.inventory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storefront.inventory.config.InventoryConstants;
import com.storefront.inventory.entity.OutboxEvent;
import com.storefront.inventory.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final StreamBridge streamBridge;
    private final OutboxService outboxService;
    
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> outboxEventList = outboxService.claimEvents();
        for (OutboxEvent outboxEvent : outboxEventList) {
            try {
                publish(outboxEvent);
            } catch (Exception e) {
                log.error("Failed to publish outbox event {}", outboxEvent.getEventId(), e);
                outboxEvent.markFailed(truncate(e.getMessage()), InventoryConstants.MAX_PUBLISH_RETRIES);
                outboxEventRepository.save(outboxEvent);
            }
        }
    }

    private void publish(OutboxEvent outboxEvent) {
        boolean sent = streamBridge.send(outboxEvent.getDestination(), outboxEvent.getPayload());
        if (!sent) 
            throw new IllegalStateException("StreamBridge returned false");
        outboxEvent.markPublished();
        outboxEventRepository.save(outboxEvent);
    }

    private String truncate(String message) {
        if (message == null) 
            return null;
        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }
    
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void recoverStuckEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(5);
        List<OutboxEvent> stuckEventList = outboxEventRepository.findStuckProcessingEvents(cutoff);
        stuckEventList.forEach(OutboxEvent::resetToPending);
    }
    
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupPublishedEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        int deleted = outboxEventRepository
        		.deletePublishedEvents(InventoryConstants.OUTBOX_STATUS_PUBLISHED, cutoff);
        log.info("Deleted {} published outbox events", deleted);
    }
    
    @Scheduled(fixedDelay = 300000)
    @Transactional(readOnly = true)
    public void monitorFailedEvents() {
        long failedCount = outboxEventRepository.countByStatus(InventoryConstants.OUTBOX_STATUS_FAILED);
        if (failedCount > 0)
            log.error("OUTBOX FAILURE ALERT - {} events in FAILED state", failedCount);
    }
    
    @Scheduled(fixedDelay = 300000)
    @Transactional(readOnly = true)
    public void monitorPendingBacklog() {
        long pendingCount = outboxEventRepository.countByStatus(InventoryConstants.OUTBOX_STATUS_PENDING);
        if (pendingCount > 1000) 
            log.warn("OUTBOX BACKLOG ALERT - {} pending events", pendingCount);
    }
    
    @Scheduled(fixedDelay = 300000)
    @Transactional(readOnly = true)
    public void monitorProcessingEvents() {
        long processingCount = outboxEventRepository.countByStatus(InventoryConstants.OUTBOX_STATUS_PROCESSING);
        if (processingCount > 100) 
            log.warn("OUTBOX PROCESSING ALERT - {} processing events", processingCount);
    }
    
}