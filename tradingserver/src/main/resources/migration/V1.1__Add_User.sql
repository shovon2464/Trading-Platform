CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);