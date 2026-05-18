--liquibase formatted sql

--changeset buyani:1.0.0.18
--preconditions onFail:MARK_RAN onError:HALT

-- Ensure the pin column does NOT exist in the user table before adding it
--precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'app_user' AND COLUMN_NAME = 'pin');

ALTER TABLE user ADD pin VARCHAR(255) DEFAULT NULL;



