--liquibase formatted sql

--changeset buyani:1.0.0.11
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'product_item' AND COLUMN_NAME = 'critical_level'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'product_item' AND COLUMN_NAME = 'generic_name'
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'product_item' AND COLUMN_NAME = 'with_prescription'

ALTER TABLE product_item ADD critical_level INT DEFAULT NULL;
ALTER TABLE product_item ADD generic_name VARCHAR(255) DEFAULT NULL;
ALTER TABLE product_item ADD with_prescription INT DEFAULT NULL;



