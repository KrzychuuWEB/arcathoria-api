package com.arcathoria.combat.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DamageTest {

    @Test
    void should_return_damage_value_when_created() {
        int expected = 10;
        Damage damage = new Damage(expected);

        assertThat(damage.value()).isEqualTo(expected);
    }

    @Test
    void should_throw_exception_when_damage_is_less_than_0() {
        assertThatThrownBy(() -> new Damage(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Damage cannot be less than 0");
    }
}