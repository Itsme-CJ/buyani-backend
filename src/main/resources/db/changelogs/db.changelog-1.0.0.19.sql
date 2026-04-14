--liquibase formatted sql

--changeset bayani:1.0.0.19
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'bayani' AND TABLE_NAME = 'store' AND COLUMN_NAME IN ('account_number', 'account_name', 'qr_code');

ALTER TABLE `store` ADD `account_number` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `store` ADD `account_name` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `store` ADD `qr_code` VARCHAR(255) DEFAULT NULL;