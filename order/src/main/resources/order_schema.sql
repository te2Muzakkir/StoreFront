CREATE DATABASE sf_order
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
    
    
CREATE TABLE orders (
    id            BIGSERIAL PRIMARY KEY,
    customer_id   BIGINT NOT NULL,
    status        VARCHAR(50) NOT NULL, -- NEW, PAID, SHIPPED
    total_amount  NUMERIC(12,2) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE order_items (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    seller_id   BIGINT NOT NULL,
    quantity    INT NOT NULL,
    price       NUMERIC(12,2) NOT NULL,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES orders(id)
);


CREATE TABLE order_saga (
    order_id     BIGSERIAL PRIMARY KEY,
    status       VARCHAR(32) NOT NULL,
    created_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_order_saga_status ON order_saga(status);
CREATE INDEX idx_order_saga_updated_at ON order_saga(updated_at);

ALTER TABLE order_saga
ADD CONSTRAINT chk_order_saga_status
CHECK (status IN (
    'PAYMENT_PENDING',
    'INVENTORY_PENDING',
    'COMPLETED',
    'FAILED'
));

CREATE TABLE order_saga_item (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGSERIAL NOT NULL,
    product_id  BIGSERIAL NOT NULL,
    sellerId  BIGSERIAL NOT NULL,
    quantity    INTEGER     NOT NULL CHECK (quantity > 0),
	reserved       BOOLEAN,
    CONSTRAINT fk_saga_item_order
        FOREIGN KEY (order_id)
        REFERENCES order_saga(order_id)
        ON DELETE CASCADE,

    CONSTRAINT uq_order_product
        UNIQUE (order_id, product_id)
);

CREATE INDEX idx_saga_item_order ON order_saga_item(order_id);

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
    version BIGINT
);

CREATE INDEX idx_outbox_status ON outbox_event(status);

CREATE INDEX idx_outbox_created_at ON outbox_event(created_at);

ALTER TABLE outbox_event ADD COLUMN processing_started_at TIMESTAMP;

CREATE TABLE order_saga_dlq (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL,
    order_id BIGINT NOT NULL,
    source VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    exception_message TEXT,
    failed_at TIMESTAMP NOT NULL);

CREATE INDEX idx_order_saga_dlq_order ON saga_failure(order_id);

CREATE INDEX idx_order_saga_dlq_source ON saga_failure(source);

CREATE INDEX idx_order_saga_dlq_failed_at ON saga_failure(failed_at);


CREATE TABLE inventory_dlq (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL,
    order_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    exception_message TEXT,
    failed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_inventory_dlq_order ON inventory_failure(order_id);

CREATE INDEX idx_inventory_dlq_event ON inventory_failure(event_id);

CREATE INDEX idx_inventory_dlq_failed_at ON inventory_failure(failed_at);