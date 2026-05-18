--liquibase formatted sql

--changeset buyani:1.0.0.12
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = 'chat_room' AND COLUMN_NAME = 'type';

ALTER TABLE chat_room ADD type INT DEFAULT NULL;



