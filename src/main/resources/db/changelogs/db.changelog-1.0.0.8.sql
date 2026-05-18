--liquibase formatted sql

--changeset buyani:1.0.0.8
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction_item' AND COLUMN_NAME = 'quantity'

ALTER TABLE `transaction_item` ADD `quantity` INT DEFAULT NULL;