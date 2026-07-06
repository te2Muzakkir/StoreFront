CREATE SEQUENCE IF NOT EXISTS sf_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users (
    id          BIGINT          NOT NULL DEFAULT nextval('sf_user_id_seq'),
    name        VARCHAR(255),
    email       VARCHAR(255) NOT NULL ,
    password    VARCHAR(255),
    role        VARCHAR(255),
    active   BOOLEAN         DEFAULT TRUE,
    created_at  TIMESTAMP,
    created_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    updated_by  VARCHAR(255),

    CONSTRAINT pk_users PRIMARY KEY (id)
);
        
ALTER TABLE users
	ADD CONSTRAINT uq_users_email UNIQUE (email),
		ALTER COLUMN email SET NOT NULL;

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

CREATE SEQUENCE sf_login_attempt_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE login_attempt (
    id BIGINT PRIMARY KEY DEFAULT nextval('sf_login_attempt_id_seq'),
    user_id BIGINT NULL,
    email_attempted VARCHAR(255) NOT NULL,
    attempt_time TIMESTAMP NOT NULL,
    successful BOOLEAN NOT NULL,
    failure_reason VARCHAR(50),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
	correlation_id VARCHAR(50)
);

CREATE INDEX idx_login_attempt_email ON login_attempt(email_attempted);
CREATE INDEX idx_login_attempt_time ON login_attempt(attempt_time);
CREATE INDEX idx_login_attempt_user ON login_attempt(user_id);
CREATE INDEX idx_login_attempt_success ON login_attempt(successful);
CREATE INDEX idx_login_attempt_email_time ON login_attempt(email_attempted, attempt_time);


CREATE SEQUENCE sf_refresh_token_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE refresh_token (
    id BIGINT PRIMARY KEY DEFAULT nextval('sf_refresh_token_seq'),
    token_id UUID NOT NULL,
    hashed_token CHAR(64) NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_used_at TIMESTAMP WITH TIME ZONE,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_refresh_token_token_id UNIQUE (token_id),
    CONSTRAINT uk_refresh_token_hash UNIQUE (hashed_token),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)

);

CREATE INDEX idx_refresh_token_user ON refresh_token(user_id);
CREATE INDEX idx_refresh_token_expiry ON refresh_token(expires_at);
CREATE INDEX idx_refresh_token_status ON refresh_token(status);

CREATE SEQUENCE sf_session_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE user_session(
    id BIGINT PRIMARY KEY DEFAULT nextval('sf_session_seq'),
    session_id VARCHAR(36) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    refresh_token_id VARCHAR(36) NOT NULL UNIQUE,
    device_name VARCHAR(100),
    browser VARCHAR(100),
    operating_system VARCHAR(100),
    ip_address VARCHAR(45),
    user_agent VARCHAR(512),
    login_at TIMESTAMP NOT NULL,
    last_activity_at TIMESTAMP NOT NULL,
    terminated_at TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    version BIGINT,
    CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_session_user ON user_session(user_id);

CREATE INDEX idx_session_status ON user_session(status);

CREATE INDEX idx_session_last_activity ON user_session(last_activity_at);

CREATE INDEX idx_session_refresh_token ON user_session(refresh_token_id);