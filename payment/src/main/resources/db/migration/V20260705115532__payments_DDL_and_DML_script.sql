CREATE SEQUENCE IF NOT EXISTS payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE payments (
    id                              BIGINT          NOT NULL DEFAULT nextval('payments_id_seq'),
    order_id                        BIGINT,
    amount                          NUMERIC(19, 2),
    status                          VARCHAR(255),
    created_at                      TIMESTAMP,
    updated_at                      TIMESTAMP,
    version                         BIGINT,
    transaction_id                  VARCHAR(255),
    gateway_transaction_id          VARCHAR(255),
    gateway_refund_transaction_id   VARCHAR(255),

    CONSTRAINT pk_payments PRIMARY KEY (id)
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
    ON payment_dlq(order_id);

CREATE INDEX idx_payment_dlq_event
    ON payment_dlq(event_id);

CREATE INDEX idx_payment_dlq_failed_at
    ON payment_dlq(failed_at);