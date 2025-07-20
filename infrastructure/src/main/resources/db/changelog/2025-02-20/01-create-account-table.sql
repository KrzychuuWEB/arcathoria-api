--liquibase formatted sql

--changeset krzysztof:1
CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);