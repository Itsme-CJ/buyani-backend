--liquibase formatted sql

--changeset buyani:1.0.0.15
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'reservation' AND COLUMN_NAME = 'reference';

ALTER TABLE `reservation` ADD `reference` VARCHAR(255) DEFAULT NULL;


