-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE new_game_creating_state
(
    initiator_chat_id             BIGINT PRIMARY KEY,
    game_name                     VARCHAR(100),
    groups_ids                    INT[],
    start_count_tasks             INT,
    max_questions_count           INT,
    max_performed_questions_count INT,
    min_questions_count_in_game   INT,
    questions_count_to_add        INT,
    max_time_minutes              INT
);

-- changeset ezuykow:2
ALTER TABLE new_game_creating_state
    ADD COLUMN requestMsgId INT;

-- changeset ezuykow:3
ALTER TABLE new_game_creating_state
    RENAME COLUMN requestMsgId TO request_msg_id;

-- changeset ezuykow:4
ALTER TABLE new_game_creating_state
    ADD COLUMN addition_with_task BOOLEAN;

-- changeset ezuykow:5
ALTER TABLE new_game_creating_state
    ADD COLUMN shuffle_questions BOOLEAN;

-- changeset ezuykow:6
ALTER TABLE new_game_creating_state
    ADD COLUMN addition_requested BOOLEAN;