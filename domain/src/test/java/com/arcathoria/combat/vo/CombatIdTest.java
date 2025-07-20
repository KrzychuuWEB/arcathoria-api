package com.arcathoria.combat.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CombatIdTest {

    @Test
    void should_return_valid_uuid() {
        UUID uuid = UUID.randomUUID();

        CombatId result = new CombatId(uuid);

        assertThat(result.value()).isEqualTo(uuid);
    }
}