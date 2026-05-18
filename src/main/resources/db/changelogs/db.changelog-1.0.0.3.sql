-- liquibase formatted sql
        
-- changeset buyani:1.0.0.3
-- validCheckSum: ANY
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM app_user
INSERT INTO store (name,description,image,status,is_reservation_activated,first_address,second_address,city,state,postal_code,phone_number,pin_location)
VALUES ('Store 1','','',1,1,'Address 1','Address 2','City 1','State','PostalCode','phone number','pinlocation');


INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id)
VALUES ('customer', 'test', 'customer@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'CUSTOMER'));

-- Inserting the 'padmin' user
INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id, store_id)
VALUES ('padmin', 'test', 'padmin@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'PADMIN'), 1);

-- Inserting the 'pcashier' user
INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id, store_id)
VALUES ('pcashier', 'test', 'pcashier@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'PCASHIER'), 1);

-- Inserting the 'admin' user
INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id)
VALUES ('admin', 'test', 'admin@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'ADMIN'));



