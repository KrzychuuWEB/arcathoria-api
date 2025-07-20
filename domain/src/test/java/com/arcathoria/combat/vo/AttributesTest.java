package com.arcathoria.combat.vo;

import com.arcathoria.character.vo.Intelligence;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AttributesTest {

    @Test
    void should_return_intelligence_level() {
        Intelligence intelligence = new Intelligence(1);

        assertThat(intelligence.getLevel()).isEqualTo(1);
    }
}