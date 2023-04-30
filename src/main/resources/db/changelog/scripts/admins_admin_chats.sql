-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE admins_admin_chats
(
    tg_admin_user_id BIGINT REFERENCES admins (tg_admin_user_id),
    tg_admin_chat_id BIGINT REFERENCES admin_chats (tg_admin_chat_id),
    PRIMARY KEY (tg_admin_user_id, tg_admin_chat_id)
);
