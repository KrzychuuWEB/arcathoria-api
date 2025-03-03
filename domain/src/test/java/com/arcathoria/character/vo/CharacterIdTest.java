package com.arcathoria.character.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterIdTest {

    @Test
    void should_create_character_id() {
        UUID uuid = UUID.randomUUID();

        CharacterId id = new CharacterId(uuid);

        assertThat(id.getValue()).isEqualTo(uuid);
    }
}