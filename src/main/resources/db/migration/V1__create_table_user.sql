CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    access_token VARCHAR(255),
    refresh_token_expires_in BIGINT,
    refresh_token VARCHAR(255),
    scope VARCHAR(255),
    token_type VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
