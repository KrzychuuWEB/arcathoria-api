--liquibase formatted sql

--changeset krzysztof:4
CREATE TABLE combats (
    id UUID PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    side VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
);