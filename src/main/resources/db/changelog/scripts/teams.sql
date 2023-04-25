-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE teams
(
    team_name VARCHAR(100) PRIMARY KEY,
    game_name VARCHAR(100) NOT NULL,
    score     INT          NOT NULL
);

-- changeset ezuykow:2
ALTER TABLE teams
    ADD COLUMN chat_id BIGINT;
ALTER TABLE teams
    DROP CONSTRAINT teams_pkey;
ALTER TABLE teams
    ADD CONSTRAINT teams_pkey PRIMARY KEY (team_name, chat_id);