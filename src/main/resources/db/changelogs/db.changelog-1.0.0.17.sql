--liquibase formatted sql

--changeset buyani:1.0.0.15
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = 'reservation' AND COLUMN_NAME IN ('schedule_time', 'schedule_day');

ALTER TABLE reservation ADD schedule_time TIMESTAMP DEFAULT NULL;
ALTER TABLE reservation ADD schedule_day TIMESTAMP DEFAULT NULL;



