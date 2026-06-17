CREATE DATABASE sf_user;

CREATE SEQUENCE IF NOT EXISTS sf_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users (
    id          BIGINT          NOT NULL DEFAULT nextval('sf_user_id_seq'),
    name        VARCHAR(255),
    email       VARCHAR(255),
    password    VARCHAR(255),
    role        VARCHAR(255),
    is_active   BOOLEAN         DEFAULT FALSE,
    created_at  TIMESTAMP,
    created_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(255),

    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uq_users_email
        UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT chk_users_role
        CHECK (role IN ('CUSTOMER', 'ADMIN'));

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role  ON users (role);


CREATE SEQUENCE IF NOT EXISTS address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE address (
    id              BIGINT          NOT NULL DEFAULT nextval('address_id_seq'),
    address         VARCHAR(255),
    landmark        VARCHAR(255),
    city            VARCHAR(255),
    state           VARCHAR(255),
    country         VARCHAR(255),
    pincode         VARCHAR(255),
    phone_number    VARCHAR(255),
    receiver_name   VARCHAR(255),
    is_default      BOOLEAN         DEFAULT FALSE,
    user_id         BIGINT,
    created_at      TIMESTAMP,
    created_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),

    CONSTRAINT pk_address PRIMARY KEY (id),
    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_address_user_id ON address (user_id);