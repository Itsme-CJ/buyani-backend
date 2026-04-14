-- liquibase formatted sql
        
-- changeset bayani:1.0.0.1
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'bayani' AND TABLE_NAME = 'user'
CREATE TABLE role (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    phone_number VARCHAR(15),
    store_id INT,
    status INT,
    role_id INT,
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

CREATE TABLE address (
    address_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    first_address VARCHAR(255) NOT NULL,
    second_address VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    pin_location VARCHAR(255),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE verification (
    verification_id INT AUTO_INCREMENT NOT NULL,
    user_id INT NOT NULL,
    code VARCHAR(255) DEFAULT NULL,
    type VARCHAR(255) DEFAULT NULL,
    expiration INT NOT NULL,
    status CHAR(3) DEFAULT NULL,
    PRIMARY KEY (verification_id),
    CHECK (expiration > 0),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT verification_fk_01 FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

CREATE TABLE store (
    store_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    status INT,
    is_reservation_activated BOOLEAN,
    first_address VARCHAR(255),
    second_address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    phone_number VARCHAR(15),
    email VARCHAR(255),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pin_location VARCHAR(255)
);

CREATE TABLE product_category (
    product_category_id INT PRIMARY KEY AUTO_INCREMENT,
    store_id INT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status INT,
    item_number INT,
    type VARCHAR(50),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE product_item (
    product_item_id INT PRIMARY KEY AUTO_INCREMENT,
    product_category_id INT,
    product_number VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    type VARCHAR(255) NULL,
    status INT,
    stock INT,
    store_id INT,
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservation_status (
    reservation_status_id INT PRIMARY KEY AUTO_INCREMENT,
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE reservation (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    store_id INT,
    status INT,
    reservation_status_id INT,
    date_claimed DATETIME,
    total_price DECIMAL(10, 2),
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (store_id) REFERENCES store(store_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_status_id) REFERENCES reservation_status(reservation_status_id)
);

CREATE TABLE reservation_list (
    reservation_list_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    product_item_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_item_id) REFERENCES product_item(product_item_id)
);

CREATE TABLE reservation_item (
    reservation_item_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT,
    product_item_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_item_id) REFERENCES product_item(product_item_id)
);

CREATE TABLE transaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    store_id INT,
    total_price DECIMAL(10, 2) NOT NULL,
    status INT,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE transaction_item (
    transaction_item_id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_id INT,
    product_item_id INT,
    FOREIGN KEY (transaction_id) REFERENCES transaction(transaction_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_item_id) REFERENCES product_item(product_item_id)
);

CREATE TABLE chat_room (
    chat_room_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    store_id INT,
    name VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE message (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    chat_room_id INT,
    content TEXT NOT NULL,
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id)
);

CREATE TABLE ratings (
    chat_room_id INT PRIMARY KEY,
    user_id INT,
    store_id INT,
    rate INT NOT NULL,
    FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE opening_hour (
    opening_hour_id INT PRIMARY KEY AUTO_INCREMENT,
    store_id INT,
    day VARCHAR(10),
    from_hour TIME,
    from_minute TIME,
    until_hour TIME,
    until_minute TIME,
    who_added VARCHAR(255),
    who_updated VARCHAR(255),
    when_added DATETIME,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);
