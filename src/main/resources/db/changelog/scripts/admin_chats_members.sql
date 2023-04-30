-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admin_chats_members
(
    tg_admin_chat_id BIGINT PRIMARY KEY,
    members          BIGINT[] NOT NULL
);
