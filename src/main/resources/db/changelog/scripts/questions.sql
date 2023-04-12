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

-- changeset ezuykow:3
ALTER TABLE questions
    ALTER COLUMN question_id TYPE INT;

-- changeset ezuykow:4
ALTER TABLE questions
DROP COLUMN "group";
ALTER TABLE  questions
    ADD COLUMN question_group TEXT NOT NULL DEFAULT 'General';

-- changeset ezuykow:5
ALTER TABLE questions
DROP COLUMN question_group;
ALTER TABLE  questions
    ADD COLUMN question_group TEXT;