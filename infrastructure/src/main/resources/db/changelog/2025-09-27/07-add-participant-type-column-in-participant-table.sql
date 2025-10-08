--liquibase formatted sql

--changeset krzysztof:6
ALTER TABLE participants
ADD COLUMN participant_type VARCHAR(15);
