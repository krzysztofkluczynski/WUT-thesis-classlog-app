CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255)
);

DROP TABLE IF EXISTS app_user;


SELECT * FROM app_user;