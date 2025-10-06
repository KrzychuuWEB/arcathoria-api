--liquibase formatted sql

--changeset krzysztof:2
CREATE TABLE characters (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
);

