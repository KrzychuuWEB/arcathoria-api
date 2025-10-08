package com.arcathoria.combat.vo;

import com.arcathoria.Level;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AttributesTest {

    @Test
    void should_return_intelligence_level() {
        Intelligence intelligence = new Intelligence(new Level(1));

        assertThat(intelligence.level().value()).isEqualTo(1);
    }
}