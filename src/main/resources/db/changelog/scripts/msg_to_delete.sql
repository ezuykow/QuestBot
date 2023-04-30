-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE msg_to_delete
(
    msg_id    INT PRIMARY KEY,
    user_id   BIGINT,
    relate_to VARCHAR(50),
    chat_id   BIGINT,
    active    BOOLEAN
);
