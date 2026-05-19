-- liquibase formatted sql

-- changeset buyani:1.0.0.26
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:integer SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'store' AND column_name = 'status'
ALTER TABLE store ALTER COLUMN status TYPE VARCHAR(50) USING CASE WHEN status = 1 THEN 'APPROVED' ELSE 'PENDING' END;

-- changeset buyani:1.0.0.27
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:integer SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'app_user' AND column_name = 'status'
ALTER TABLE app_user ALTER COLUMN status TYPE VARCHAR(50) USING CASE WHEN status = 1 THEN 'ACTIVE' ELSE 'INACTIVE' END;

-- changeset buyani:1.0.0.28
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:integer SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'app_user' AND column_name = 'store_id'
ALTER TABLE app_user ALTER COLUMN store_id TYPE VARCHAR(50) USING store_id::text;

-- changeset buyani:1.0.0.29
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:integer SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'product_category' AND column_name = 'status'
ALTER TABLE product_category ALTER COLUMN status TYPE VARCHAR(50) USING status::text;

-- changeset buyani:1.0.0.30
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:integer SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'product_item' AND column_name = 'status'
ALTER TABLE product_item ALTER COLUMN status TYPE VARCHAR(50) USING status::text;
