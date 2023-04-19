-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admins
(
    tg_admin_user_id BIGINT PRIMARY KEY,
    is_owner         boolean NOT NULL
);
