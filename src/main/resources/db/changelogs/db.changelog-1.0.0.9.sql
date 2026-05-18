--liquibase formatted sql

--changeset buyani:1.0.0.9
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'reservation' AND COLUMN_NAME = 'transaction_id'

ALTER TABLE reservation ADD transaction_id INT DEFAULT NULL;



