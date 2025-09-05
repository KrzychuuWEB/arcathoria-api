package com.arcathoria.character.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelTest {

    @Test
    void should_return_valid_level() {
        int exampleLevel = 10;

        Level level = new Level(exampleLevel);

        assertThat(level.level()).isEqualTo(exampleLevel);
    }

    @Test
    void sohuld_return_new_level_after_increment() {
        Level level = new Level(1);

        Level newLevel = level.increaseBy(1);

        assertThat(newLevel.level()).isEqualTo(2);
    }

    @Test
    void should_return_exception_when_level_is_below_zero() {
        Level level = new Level(1);

        assertThatThrownBy(() -> level.increaseBy(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Level delta must be greater than 0");
    }

    @Test
    void should_return_next_level_with_one_level_increment() {
        Level level = new Level(1);

        Level nextLevel = level.next();

        assertThat(nextLevel.level()).isEqualTo(2);
    }
}