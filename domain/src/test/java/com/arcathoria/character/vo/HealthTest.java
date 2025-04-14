package com.arcathoria.character.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HealthTest {

    @Test
    void should_return_max_and_current() {
        Double current = 100.0;
        Double max = 150.0;

        Health result = new Health(current, max);

        assertThat(result.getCurrent()).isEqualTo(current);
        assertThat(result.getMax()).isEqualTo(max);
    }

    @Test
    void should_return_exception_when_current_and_max_is_below_zero() {
        assertThatThrownBy(() -> new Health(-0.1, -0.10))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Resource cannot be less than 0");
    }

    @Test
    void should_return_exception_when_current_current_is_greater_than_maximum() {
        assertThatThrownBy(() -> new Health(20.0, 10.0))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("The current value cannot be greater than the maximum");
    }

    @Test
    void should_add_value_and_return_new_current_value() {
        Health health = new Health(80.0, 100.0);
        health.add(10.0);

        assertThat(health.getCurrent()).isEqualTo(90.0);
    }

    @Test
    void should_return_exception_when_new_value_is_below_zero() {
        Health health = new Health(100.0, 100.0);

        assertThatThrownBy(() -> health.add(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Added value cannot be less than 0");
    }

    @Test
    void should_return_max_value_when_current_plus_added_is_greater_than_max() {
        Health health = new Health(100.0, 100.0);
        health.add(10.0);

        assertThat(health.getCurrent()).isEqualTo(100.0);
    }

    @Test
    void should_reduced_value_and_return_new_current_value() {
        Health health = new Health(100.0, 100.0);
        health.subtract(10.0);

        assertThat(health.getCurrent()).isEqualTo(90.0);
    }

    @Test
    void should_return_exception_when_subtract_value_is_below_zero() {
        Health health = new Health(100.0, 100.0);

        assertThatThrownBy(() -> health.subtract(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .message().isEqualTo("Subtracted value cannot be less than 0");
    }

    @Test
    void should_set_zero_for_current_value_when_current_value_minus_subtract_is_below_zero() {
        Health health = new Health(10.0, 100.0);
        health.subtract(20.0);

        assertThat(health.getCurrent()).isEqualTo(0.0);
    }
}