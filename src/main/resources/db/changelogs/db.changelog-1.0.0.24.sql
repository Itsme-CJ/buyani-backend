-- liquibase formatted sql

-- changeset buyani:1.0.0.24
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = 'app_user' AND COLUMN_NAME = 'email_verified'
ALTER TABLE app_user ADD COLUMN email_verified BOOLEAN DEFAULT FALSE;
