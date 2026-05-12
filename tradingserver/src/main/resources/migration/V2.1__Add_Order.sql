CREATE TABLE order (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    price INT NOT NULL,
    order_type VARCHAR(50) NOT NULL,
    created_date DATETIME,
    remaining_balance BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES user(id)
);