package com.arcathoria.character.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntelligenceTest {

    @Test
    void should_return_correct_level() {
        Intelligence intelligence = new Intelligence(1);

        assertThat(intelligence.getLevel()).isEqualTo(1);
    }

    @Test
    void should_return_exception_when_level_is_below_zero() {
        assertThatThrownBy(() -> new Intelligence(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Attribute cannot be less than 0");
    }

    @Test
    void should_return_new_level_after_add() {
        Intelligence intelligence = new Intelligence(1);

        intelligence.addLevel(1);

        int newLevel = intelligence.getLevel();

        assertThat(newLevel).isEqualTo(2);
    }
}