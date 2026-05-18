--liquibase formatted sql

--changeset buyani:1.0.0.19
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME IN ('authorized_by', 'transaction_num');

ALTER TABLE `transaction` ADD `authorized_by` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `transaction` ADD `transaction_num` VARCHAR(255) DEFAULT NULL;

