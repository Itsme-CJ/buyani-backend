-- liquibase formatted sql
        
-- changeset buyani:1.0.0.4
-- preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'transaction' AND COLUMN_NAME = 'discount'
ALTER TABLE `transaction` ADD discount DECIMAL(10, 2) DEFAULT NULL;


