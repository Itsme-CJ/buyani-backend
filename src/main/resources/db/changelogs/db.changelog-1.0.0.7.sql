--liquibase formatted sql

--changeset buyani:1.0.0.7
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = 'transaction_item'

ALTER TABLE transaction_item 
DROP FOREIGN KEY transaction_item_ibfk_1;

ALTER TABLE transaction_item 
ADD CONSTRAINT transaction_item_ibfk_1
FOREIGN KEY (transaction_id) REFERENCES transaction(transaction_id) ON DELETE CASCADE;



