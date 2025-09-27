--liquibase formatted sql

--changeset krzysztof:6
ALTER TABLE participants
ADD COLUMN participantType VARCHAR(15);
