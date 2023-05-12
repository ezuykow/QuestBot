-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE tasks
(
    task_id             SERIAL PRIMARY KEY,
    game_name           VARCHAR(100) NOT NULL,
    question_id         INT UNIQUE   NOT NULL,
    performed_team_name VARCHAR(100)
);

-- changeset ezuykow:2
ALTER TABLE tasks
    ADD COLUMN chat_id BIGINT;

-- changeset ezuykow:3
ALTER TABLE tasks
    ADD COLUMN actual BOOLEAN;

-- changeset ezuykow:4
ALTER TABLE tasks
    ADD COLUMN task_number INT;