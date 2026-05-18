--liquibase formatted sql

--changeset buyani:1.0.0.14
--preconditions onFail:MARK_RAN onError:HALT

-- Ensure `chat_room` exists
--precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'chat_room');

-- Ensure `message` exists
--precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'buyani' AND TABLE_NAME = 'message');

ALTER TABLE chat_room 
DROP FOREIGN KEY chat_room_ibfk_2;

ALTER TABLE chat_room 
ADD CONSTRAINT chat_room_ibfk_2
FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE;

ALTER TABLE message DROP FOREIGN KEY message_ibfk_1;

ALTER TABLE message
ADD CONSTRAINT message_ibfk_1
FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id) ON DELETE CASCADE;

ALTER TABLE product_category DROP FOREIGN KEY product_category_ibfk_1;
ALTER TABLE product_category
ADD CONSTRAINT product_category_ibfk_1
FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE;

ALTER TABLE transaction DROP FOREIGN KEY transaction_ibfk_2;
ALTER TABLE transaction
ADD CONSTRAINT transaction_ibfk_2
FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE;

-- ALTER TABLE message 
-- DROP FOREIGN KEY message_ibfk_2;

-- ALTER TABLE message 
-- ADD CONSTRAINT message_ibfk_2
-- FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id) ON DELETE CASCADE;

-- ALTER TABLE opening_hour 
-- DROP FOREIGN KEY opening_hour_ibfk_2;

-- ALTER TABLE opening_hour 
-- ADD CONSTRAINT opening_hour_ibfk_2
-- FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE;