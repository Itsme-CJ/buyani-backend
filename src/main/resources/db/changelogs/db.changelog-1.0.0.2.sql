-- liquibase formatted sql
        
-- changeset buyani:1.0.0.2
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM  role
USE BuyAni;
INSERT INTO `role` (name) VALUES ('CUSTOMER'), ('PADMIN'), ('PCASHIER'), ('ADMIN');
