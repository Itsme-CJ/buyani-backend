--liquibase formatted sql

--changeset bayani:1.0.0.9
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'bayani' AND TABLE_NAME = 'reservation' AND COLUMN_NAME = 'transaction_id'

ALTER TABLE `reservation` ADD `transaction_id` INT DEFAULT NULL;