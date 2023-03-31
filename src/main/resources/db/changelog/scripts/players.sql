-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE players (
    tg_user_id BIGINT PRIMARY KEY ,
    game_name VARCHAR(100) REFERENCES games(game_name) ,
    team_name VARCHAR(100) REFERENCES teams(team_name)
)