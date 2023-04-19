-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE global_chats
(
    tg_chat_id BIGINT PRIMARY KEY
);

-- changeset ezuykow:2
ALTER TABLE global_chats
    ADD COLUMN creating_game_name VARCHAR(100);
ALTER TABLE  global_chats
    ADD COLUMN is_game_started BOOLEAN;
ALTER TABLE global_chats
    ADD COLUMN minutes_since_start INT;
