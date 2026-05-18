-- liquibase formatted sql

-- changeset buyani:1.0.0.25
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = 'app_user' AND COLUMN_NAME = 'pin'
ALTER TABLE app_user ADD COLUMN pin INT DEFAULT NULL;
