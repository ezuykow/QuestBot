-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE games
(
    game_name                     VARCHAR(100) PRIMARY KEY,
    global_chat_id                BIGINT,
    groups_ids                    INT[],
    max_time_minutes              INT CHECK ( max_time_minutes > 0 ),
    max_questions_count           INT CHECK ( max_questions_count > 0 ),
    max_performed_questions_count INT CHECK ( max_performed_questions_count > 0 ),
    min_questions_count_in_game   INT CHECK ( min_questions_count_in_game > -1 ),
    questions_count_to_add        INT CHECK ( questions_count_to_add > -1 ),
    is_started                    BOOLEAN,
    start_count_tasks             INT CHECK ( start_count_tasks > 0 )
);

-- changeset ezuykow:2
ALTER TABLE games
    DROP COLUMN global_chat_id;
ALTER TABLE games
    DROP COLUMN is_started;

-- changeset ezuykow:3
ALTER TABLE games
    ADD COLUMN addition_with_task BOOLEAN DEFAULT true;
