CREATE DATABASE sf_product
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
    
    
CREATE TABLE category (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150) NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now(),
    active       BOOLEAN DEFAULT true
);

CREATE TABLE products (
    id           BIGSERIAL PRIMARY KEY,
    seller_id    BIGINT NOT NULL,
    category_id  BIGINT NOT NULL,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    price        NUMERIC(12,2) NOT NULL,
    active       BOOLEAN DEFAULT true,
    created_at   TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE INDEX idx_products_name ON products (name);

CREATE INDEX idx_products_name_fts ON products USING GIN (to_tsvector('english', name));