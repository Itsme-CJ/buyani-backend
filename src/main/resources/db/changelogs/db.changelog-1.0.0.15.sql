--liquibase formatted sql

--changeset buyani:1.0.0.16
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'reservation_list' AND COLUMN_NAME = 'store_id';

ALTER TABLE `reservation_list` ADD `store_id` INT DEFAULT NULL;


