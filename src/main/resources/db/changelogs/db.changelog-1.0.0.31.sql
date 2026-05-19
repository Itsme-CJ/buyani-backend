-- liquibase formatted sql

-- changeset buyani:1.0.0.31
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'notification'
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    title VARCHAR(255),
    message TEXT,
    type VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset buyani:1.0.0.32
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'audit_log'
CREATE TABLE audit_log (
    audit_log_id SERIAL PRIMARY KEY,
    entity_id INT,
    store_id INT,
    entity_name VARCHAR(255),
    operation VARCHAR(255),
    field VARCHAR(255),
    previous_value TEXT,
    new_value TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id VARCHAR(255)
);
