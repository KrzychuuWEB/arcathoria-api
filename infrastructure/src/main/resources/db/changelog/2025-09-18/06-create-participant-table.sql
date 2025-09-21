--liquibase formatted sql

--changeset krzysztof:5
CREATE TABLE participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    combat_id UUID NOT NULL,
    character_id UUID NOT NULL,
    side VARCHAR(20) NOT NULL,
    current_health INTEGER NOT NULL,
    max_health INTEGER NOT NULL,
    intelligence INTEGER NOT NULL,
    CONSTRAINT fk_combat FOREIGN KEY (combat_id) REFERENCES combats(id) ON DELETE CASCADE
);