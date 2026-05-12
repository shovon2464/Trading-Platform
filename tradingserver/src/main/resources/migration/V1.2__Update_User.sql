ALTER TABLE user
    ADD COLUMN login_pin VARCHAR(8),
    ADD COLUMN phone_number VARCHAR(20),
    ADD COLUMN birthday DATE,
    ADD COLUMN biometric_key VARCHAR(255),
    ADD COLUMN gender VARCHAR(50),
    ADD COLUMN wrong_pin_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN blocked_until_pin DATETIME,
    ADD COLUMN wrong_password_attempts INT NOT NULL DEFAULT 0,
    ADD COLUMN blocked_until_password DATETIME,
    ADD COLUMN balance BIGINT DEFAULT 0,
    ADD COLUMN created_date DATETIME,
    ADD COLUMN updated_date DATETIME;

ALTER TABLE user
    ADD CONSTRAINT chk_login_pin_length
    CHECK (login_pin IS NULL OR CHAR_LENGTH(login_pin) BETWEEN 4 AND 8);