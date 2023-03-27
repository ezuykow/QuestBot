-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admin_chats(
    admin_chat_id BIGSERIAL PRIMARY KEY ,
    tg_chat_id BIGINT
);

-- changeset ezuykow:2
ALTER TABLE admin_chats
    ALTER COLUMN tg_chat_id SET NOT NULL ;

-- changeset ezuykow:3
DROP TABLE admin_chats;
CREATE TABLE admin_chats(
    tg_admin_chat_id BIGINT PRIMARY KEY
);