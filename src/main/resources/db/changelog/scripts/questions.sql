-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE questions(
    question_id BIGSERIAL PRIMARY KEY ,
    question TEXT NOT NULL UNIQUE ,
    answer_format VARCHAR(100) NOT NULL,
    answer VARCHAR(100) NOT NULL ,
    map_url TEXT ,
    last_usage DATE
);

-- changeset ezuykow:2
ALTER TABLE questions
    ADD COLUMN "group" TEXT NOT NULL DEFAULT 'General';