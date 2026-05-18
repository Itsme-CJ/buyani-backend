--liquibase formatted sql

--changeset buyani:1.0.0.5
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME = 'cash'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME = 'change'

ALTER TABLE `transaction` ADD `cash` DECIMAL(10, 2) DEFAULT NULL;
ALTER TABLE `transaction` ADD `change` DECIMAL(10, 2) DEFAULT NULL;