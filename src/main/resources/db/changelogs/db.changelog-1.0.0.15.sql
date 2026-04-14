--liquibase formatted sql

--changeset bayani:1.0.0.16
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'bayani' AND TABLE_NAME = 'reservation_list' AND COLUMN_NAME = 'store_id';

ALTER TABLE `reservation_list` ADD `store_id` INT DEFAULT NULL;