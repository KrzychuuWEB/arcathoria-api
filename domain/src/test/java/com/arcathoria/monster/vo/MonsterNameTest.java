package com.arcathoria.monster.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonsterNameTest {

    @Test
    void should_return_value_for_correct_name() {
        String name = "Wolf";

        MonsterName result = new MonsterName(name);

        assertThat(result.value()).isEqualTo(name);
    }

    @Test
    void should_return_exception_when_name_is_null() {
        assertThatThrownBy(() -> new MonsterName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Monster name is required and cannot be empty");
    }

    @Test
    void should_return_exception_when_name_is_empty() {
        assertThatThrownBy(() -> new MonsterName(""))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Monster name is required and cannot be empty");
    }
}