package com.arcathoria.combat.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MonsterIdTest {
    @Test
    void should_create_account_id_when_valid_uuid() {
        UUID uuid = UUID.randomUUID();

        MonsterId id = new MonsterId(uuid);

        assertThat(id.value()).isEqualTo(uuid);
    }
}