-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admin_chats(
    admin_chat_id BIGSERIAL PRIMARY KEY ,
    tg_chat_id BIGINT
);