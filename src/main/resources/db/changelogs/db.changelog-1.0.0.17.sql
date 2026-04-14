--liquibase formatted sql

--changeset bayani:1.0.0.15
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'bayani' AND TABLE_NAME = 'reservation' AND COLUMN_NAME IN ('schedule_time', 'schedule_day');

ALTER TABLE `reservation` ADD `schedule_time` DATETIME DEFAULT NULL;
ALTER TABLE `reservation` ADD `schedule_day` DATETIME DEFAULT NULL;