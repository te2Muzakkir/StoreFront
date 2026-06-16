CREATE DATABASE sf_inventory
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
    
CREATE TABLE inventory (
    product_id   BIGINT NOT NULL,
    seller_id    BIGINT NOT NULL,
    total_quantity     INT    NOT NULL DEFAULT 0,
    updated_at   TIMESTAMP NOT NULL DEFAULT now(),
	reserved_quantity INT NOT NULL,
	version BIGINT NOT NULL,
    PRIMARY KEY (product_id, seller_id)
);


CREATE TABLE inventory_movement (
    id               BIGSERIAL PRIMARY KEY,
    product_id       BIGINT NOT NULL,
    seller_id        BIGINT NOT NULL,
    quantity_change  INT    NOT NULL,
    movement_type    VARCHAR(10) NOT NULL, -- IN / OUT
    reference        VARCHAR(100),          -- ORDER_ID / MANUAL
    event_id        VARCHAR(100),
    created_at       TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_inventory_movement_event_id ON inventory_movement(event_id);

CREATE INDEX idx_inventory_movement_reference ON inventory_movement(reference);

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

CREATE INDEX idx_outbox_status ON outbox_event(status);

CREATE INDEX idx_outbox_created_at ON outbox_event(created_at);