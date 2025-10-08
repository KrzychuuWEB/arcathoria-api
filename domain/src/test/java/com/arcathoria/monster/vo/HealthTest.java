package com.arcathoria.monster.vo;

import com.arcathoria.Gauge;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HealthTest {

    @Test
    void should_return_exception_when_health_gauge_is_null() {
        assertThatThrownBy(() -> new Health(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Health gauge cannot be null");
    }

    @Test
    void should_return_max_and_current() {
        int current = 100;
        int max = 150;

        Health result = new Health(new Gauge(current, max));

        assertThat(result.getCurrent()).isEqualTo(current);
        assertThat(result.getMax()).isEqualTo(max);
    }

    @Test
    void should_return_exception_when_current_and_max_is_below_zero() {
        assertThatThrownBy(() -> new Health(new Gauge(-1, -1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Max must be > 0");
    }

    @Test
    void should_return_exception_when_current_current_is_greater_than_maximum() {
        assertThatThrownBy(() -> new Health(new Gauge(20, 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current value must be <= max");
    }

    @Test
    void should_add_value_and_return_new_current_value() {
        Health health = new Health(new Gauge(80, 100));
        health = health.add(10);

        assertThat(health.getCurrent()).isEqualTo(90);
    }

    @Test
    void should_return_exception_when_add_value_is_less_than_0() {
        Health health = new Health(new Gauge(100, 100));

        assertThatThrownBy(() -> health.add(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Add value must be >= 0");
    }

    @Test
    void should_return_max_value_when_current_plus_added_is_greater_than_max() {
        Health health = new Health(new Gauge(100, 100));
        health = health.add(10);

        assertThat(health.getCurrent()).isEqualTo(100);
    }

    @Test
    void should_return_exception_when_subtract_value_is_less_than_0() {
        Health health = new Health(new Gauge(100, 100));

        assertThatThrownBy(() -> health.subtract(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Subtract value must be >= 0");
    }

    @Test
    void should_reduced_value_and_return_new_current_value() {
        Health health = new Health(new Gauge(100, 100));
        health = health.subtract(10);

        assertThat(health.getCurrent()).isEqualTo(90);
    }

    @Test
    void should_set_zero_for_current_value_when_current_value_minus_subtract_is_below_zero() {
        Health health = new Health(new Gauge(10, 100));
        health = health.subtract(20);

        assertThat(health.getCurrent()).isZero();
    }

    @Test
    void should_return_false_if_health_is_equal_to_0() {
        Health health = new Health(new Gauge(10, 10));
        health = health.subtract(10);

        assertThat(health.isAlive()).isFalse();
    }

    @Test
    void should_return_true_if_health_is_greater_than_0() {
        Health health = new Health(new Gauge(10, 10));

        assertThat(health.isAlive()).isTrue();
    }
}