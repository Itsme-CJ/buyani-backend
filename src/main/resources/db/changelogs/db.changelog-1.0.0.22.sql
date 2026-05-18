--liquibase formatted sql

--changeset buyani:1.0.0.22
--preconditions onFail:MARK_RAN onError:HALT

-- Ensure the trigger does not already exist
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.TRIGGERS WHERE TRIGGER_SCHEMA = 'buyani' AND TRIGGER_NAME = 'before_store_delete';

CREATE TRIGGER before_store_delete
BEFORE DELETE ON `store`
FOR EACH ROW
BEGIN
    DELETE FROM `reservation` WHERE `store_id` = OLD.`store_id`;

    DELETE FROM `reservation_list` WHERE `store_id` = OLD.`store_id`;
   
   DELETE FROM `product_category` WHERE `store_id` = OLD.`store_id`;
   
   DELETE FROM `transaction` WHERE `store_id` = OLD.`store_id`;
  
   DELETE FROM `chat_room` WHERE `store_id` = OLD.`store_id`;
   
   DELETE FROM `opening_hour` WHERE `store_id` = OLD.`store_id`;

END;


