CREATE DATABASE sf_payment
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
    
    
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, SUCCESS, FAILED
    paid_at TIMESTAMP
);

CREATE TABLE processed_event (
    event_id      VARCHAR(36) PRIMARY KEY,
    processed_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE outbox_event (

    event_id VARCHAR(36) PRIMARY KEY,

    aggregate_type VARCHAR(100) NOT NULL,

    aggregate_id VARCHAR(100) NOT NULL,

    destination VARCHAR(255) NOT NULL,

    payload TEXT NOT NULL,

    status VARCHAR(20) NOT NULL,

    retry_count INTEGER NOT NULL DEFAULT 0,

    last_error TEXT,

    created_at TIMESTAMP NOT NULL,

    published_at TIMESTAMP,

    processing_started_at TIMESTAMP,

    version BIGINT
);

CREATE INDEX idx_outbox_status
    ON outbox_event(status);

CREATE INDEX idx_outbox_created_at
    ON outbox_event(created_at);
    
CREATE TABLE payment_dlq (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL,
    order_id BIGINT NOT NULL,
    payment_action VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    exception_message TEXT,
    failed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_payment_dlq_order
    ON payment_failure(order_id);

CREATE INDEX idx_payment_dlq_event
    ON payment_failure(event_id);

CREATE INDEX idx_payment_dlq_failed_at
    ON payment_failure(failed_at);