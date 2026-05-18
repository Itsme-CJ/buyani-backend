-- liquibase formatted sql

-- changeset buyani:1.0.0.23
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM app_user WHERE email_address = 'padmin@gmail.com'
INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id)
VALUES ('customer', 'test', 'customer@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'CUSTOMER'));

INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id, store_id)
VALUES ('padmin', 'test', 'padmin@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'PADMIN'), 1);

INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id, store_id)
VALUES ('pcashier', 'test', 'pcashier@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'PCASHIER'), 1);

INSERT INTO app_user (first_name, last_name, email_address, password, status, role_id)
VALUES ('admin', 'test', 'admin@gmail.com', '$2a$10$eKv1PjeuW96zZezHdFBAhO3OEhPV9hqFMsrufnxncviREVUAP1EKC', 1,
  (SELECT role_id FROM role WHERE name = 'ADMIN'));
