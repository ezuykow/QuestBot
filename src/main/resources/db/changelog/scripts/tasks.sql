-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY ,
    game_name VARCHAR(100) NOT NULL ,
    question_id BIGINT UNIQUE NOT NULL ,
    performed_team_name VARCHAR(100)
);
