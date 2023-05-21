-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE questions
(
    question_id    SERIAL PRIMARY KEY,
    question       TEXT UNIQUE  NOT NULL,
    answer_format  VARCHAR(100) NOT NULL,
    answer         VARCHAR(100) NOT NULL,
    map_url        TEXT,
    question_group TEXT,
    last_usage     DATE
);


-- changeset ezuykow:2
ALTER TABLE questions
    ALTER COLUMN answer_format DROP NOT NULL;