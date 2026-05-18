CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    user_role VARCHAR(50) DEFAULT 'CUSTOMER',
    phone_number VARCHAR(255),
    gender VARCHAR(50),
    created_date DATETIME,
    updated_date DATETIME,
    PRIMARY KEY (id)
);