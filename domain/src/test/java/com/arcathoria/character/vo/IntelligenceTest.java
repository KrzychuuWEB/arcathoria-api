package com.arcathoria.character.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntelligenceTest {

    @Test
    void should_return_exception_when_level_is_null() {
        assertThatThrownBy(() -> new Intelligence(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Intelligence level cannot be null");
    }

    @Test
    void should_return_correct_level() {
        Intelligence intelligence = new Intelligence(new Level(1));

        assertThat(intelligence.level().value()).isEqualTo(1);
    }

    @Test
    void should_return_exception_when_level_is_below_zero() {
        assertThatThrownBy(() -> new Intelligence(new Level(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Level must be greater than 0");
    }

    @Test
    void should_return_new_level_after_add() {
        Intelligence intelligence = new Intelligence(new Level(1));

        intelligence = intelligence.increaseBy(1);

        int newLevel = intelligence.level().value();

        assertThat(newLevel).isEqualTo(2);
    }
}