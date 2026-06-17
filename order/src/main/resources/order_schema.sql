CREATE DATABASE sf_order
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
    
    
CREATE SEQUENCE IF NOT EXISTS orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE orders (
    id               BIGINT          NOT NULL DEFAULT nextval('orders_id_seq'),
    customer_id      BIGINT,
    status           VARCHAR(255),
    total_amount     NUMERIC(19, 2),
    created_at       TIMESTAMP,
    shipping_address VARCHAR(255),
    billing_address  VARCHAR(255),
    payment_mode     VARCHAR(255),

    CONSTRAINT pk_orders PRIMARY KEY (id)
);


CREATE SEQUENCE IF NOT EXISTS order_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE order_items (
    id          BIGINT          NOT NULL DEFAULT nextval('order_items_id_seq'),
    order_id    BIGINT,
    product_id  BIGINT,
    seller_id   BIGINT,
    quantity    INTEGER,
    price       NUMERIC(19, 2),

    CONSTRAINT pk_order_items PRIMARY KEY (id)
);

CREATE TABLE order_saga (
    order_id                    BIGINT          NOT NULL,
    status                      VARCHAR(255),
    created_at                  TIMESTAMP,
    updated_at                  TIMESTAMP,
    payment_command_sent        BOOLEAN         DEFAULT FALSE,
    inventory_confirm_sent      BOOLEAN         DEFAULT FALSE,
    inventory_release_sent      BOOLEAN         DEFAULT FALSE,
    recovery_retry_count        INTEGER         DEFAULT 0,
    last_recovery_attempt_at    TIMESTAMP,
    CONSTRAINT pk_order_saga PRIMARY KEY (order_id)
);

CREATE INDEX idx_order_saga_status ON order_saga(status);
CREATE INDEX idx_order_saga_updated_at ON order_saga(updated_at);;

CREATE TABLE order_saga_item (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGSERIAL NOT NULL,
    product_id  BIGSERIAL NOT NULL,
    seller_id  BIGSERIAL NOT NULL,
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

CREATE INDEX idx_order_saga_dlq_order ON order_saga_dlq(order_id);

CREATE INDEX idx_order_saga_dlq_source ON order_saga_dlq(source);

CREATE INDEX idx_order_saga_dlq_failed_at ON order_saga_dlq(failed_at);


CREATE SEQUENCE IF NOT EXISTS dlq_audit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
CREATE TABLE dlq_audit (
    id              BIGINT          NOT NULL DEFAULT nextval('dlq_audit_id_seq'),
    event_id        VARCHAR(255),
    order_id        VARCHAR(255),
    source          VARCHAR(255),
    payload         TEXT,
    status          VARCHAR(255),
    replay_count    INTEGER,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,

    CONSTRAINT pk_dlq_audit PRIMARY KEY (id)
);