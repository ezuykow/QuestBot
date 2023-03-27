-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE question_groups(
    group_name VARCHAR(100) PRIMARY KEY
);

-- changeset ezuykow:2
INSERT INTO question_groups
    VALUES ('General');

-- changeset ezuykow:3
DROP TABLE question_groups;
CREATE TABLE question_groups(
    group_id SERIAL PRIMARY KEY ,
    group_name VARCHAR(100) UNIQUE NOT NULL
);
INSERT INTO question_groups(group_name)
    VALUES ('General');