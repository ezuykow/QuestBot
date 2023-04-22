-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE question_groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(100) UNIQUE NOT NULL
);
