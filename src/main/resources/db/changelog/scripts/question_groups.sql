-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE question_groups(
    group_name VARCHAR(100) PRIMARY KEY
);

-- changeset ezuykow:2
INSERT INTO question_groups
    VALUES ('General');