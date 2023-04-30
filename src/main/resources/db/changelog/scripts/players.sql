-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE players
(
    tg_user_id BIGINT PRIMARY KEY,
    game_name  VARCHAR(100) NOT NULL,
    team_name  VARCHAR(100) NOT NULL
);

-- changeset ezuykow:2
ALTER TABLE players
    ADD COLUMN chat_id BIGINT;