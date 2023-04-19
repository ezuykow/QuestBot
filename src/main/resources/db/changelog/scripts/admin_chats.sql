-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admin_chats
(
    tg_admin_chat_id BIGINT PRIMARY KEY,
    blocked_by_admin_id BIGINT
);
