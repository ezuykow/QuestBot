-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE teams (
    team_name VARCHAR(100) PRIMARY KEY ,
    game_name VARCHAR(100) NOT NULL ,
    score INT NOT NULL DEFAULT 0
);
