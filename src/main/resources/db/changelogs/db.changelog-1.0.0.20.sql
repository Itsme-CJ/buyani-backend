--liquibase formatted sql

-- changeset bayani:1.0.0.20
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'bayani' AND TABLE_NAME = 'audit_log'
CREATE TABLE audit_log (
    audit_log_id INT PRIMARY KEY AUTO_INCREMENT,
    entity_id INT DEFAULT NULL,
    store_id INT DEFAULT NULL,
    user_id INT DEFAULT NULL,
    entity_name VARCHAR(256) DEFAULT NULL,
    operation VARCHAR(256) DEFAULT NULL,
    field VARCHAR(256) DEFAULT NULL,
    previous_value NVARCHAR(800) DEFAULT NULL,
    new_value NVARCHAR(800) DEFAULT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);