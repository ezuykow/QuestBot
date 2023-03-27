-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admins(
    admin_id BIGSERIAL PRIMARY KEY ,
    tg_user_id BIGINT NOT NULL ,
    tg_admin_chat_id BIGINT NOT NULL
);

-- changeset ezuykow:2
ALTER TABLE admins
    DROP COLUMN tg_admin_chat_id;

-- changeset ezuykow:3
DROP TABLE admins;
CREATE TABLE admins(
    tg_admin_user_id BIGINT PRIMARY KEY
);