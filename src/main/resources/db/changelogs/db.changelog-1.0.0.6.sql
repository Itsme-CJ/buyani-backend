--liquibase formatted sql

--changeset buyani:1.0.0.6
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME = 'change'
ALTER TABLE transaction CHANGE `change` `transaction_change` DECIMAL(10, 2) DEFAULT NULL;


