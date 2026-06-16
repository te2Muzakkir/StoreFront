CREATE DATABASE sf_user;

CREATE SEQUENCE sf_user_seq START 1 INCREMENT 1;

CREATE SEQUENCE address_seq START 1 INCREMENT 1;


CREATE TABLE sf_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),

    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
);


CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,

    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,

    user_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),

    updated_at TIMESTAMP,
    updated_by VARCHAR(100),

    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id)
        REFERENCES sf_user(id)
        ON DELETE CASCADE
);
