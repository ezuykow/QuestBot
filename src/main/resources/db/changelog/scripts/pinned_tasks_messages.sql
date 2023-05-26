-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE pinned_tasks_messages
(
    message_id INT PRIMARY KEY,
    chat_id BIGINT NOT NULL
)