CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    event_id VARCHAR(255),
    summary VARCHAR(255),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    users_id BIGINT,
    CONSTRAINT fk_events_users FOREIGN KEY (users_id) REFERENCES users(id)
);
