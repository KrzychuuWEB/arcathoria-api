--liquibase formatted sql

--changeset krzysztof:3
ALTER TABLE characters
ADD COLUMN intelligence INT;

