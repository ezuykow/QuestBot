-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE question_groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(100) UNIQUE NOT NULL
);

-- changeset ezuykow:2
INSERT INTO question_groups(group_name) values ('Вопросы без группы');
