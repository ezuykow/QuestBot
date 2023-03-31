-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY ,
    game_name VARCHAR(100) REFERENCES games(game_name) ,
    question_id BIGINT UNIQUE REFERENCES questions(question_id) ,
    performed_team_name VARCHAR(100)
)