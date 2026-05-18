--liquibase formatted sql

--changeset buyani:1.0.0.10
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME = 'cust_name'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME = 'cust_id'

ALTER TABLE `transaction` ADD `cust_name` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `transaction` ADD `cust_id` VARCHAR(255) DEFAULT NULL;

