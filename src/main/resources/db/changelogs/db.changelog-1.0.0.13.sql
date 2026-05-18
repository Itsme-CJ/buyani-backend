--liquibase formatted sql

--changeset buyani:1.0.0.13
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'message' AND COLUMN_NAME = 'to';

ALTER TABLE message ADD to VARCHAR(255) DEFAULT NULL;



