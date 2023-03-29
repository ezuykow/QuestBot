-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE global_chats(
    tg_chat_id BIGSERIAL PRIMARY KEY
);

-- changeset ezuykow:2
DROP TABLE global_chats;
CREATE TABLE global_chats(
    tg_chat_id BIGINT PRIMARY KEY
);
