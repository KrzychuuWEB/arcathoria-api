package com.arcathoria;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GaugeTest {

    @Test
    void should_create_gauge_when_valid_input() {
        int current = 50;
        int max = 100;

        Gauge gauge = new Gauge(current, max);

        assertThat(gauge.current()).isEqualTo(current);
        assertThat(gauge.max()).isEqualTo(max);
    }

    @Test
    void should_throw_exception_when_max_is_less_than_0() {
        assertThatThrownBy(() -> new Gauge(-1, -10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Max must be > 0");
    }

    @Test
    void should_throw_exception_when_current_is_less_than_0() {
        assertThatThrownBy(() -> new Gauge(-10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current value must be >= 0");
    }

    @Test
    void should_throw_exception_when_current_is_greater_than_max() {
        assertThatThrownBy(() -> new Gauge(20, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current value must be <= max");
    }

    @Test
    void should_add_value_and_return_new_current_value() {
        Gauge gauge = new Gauge(50, 100);

        gauge = gauge.add(50);

        assertThat(gauge.current()).isEqualTo(100);
    }

    @Test
    void should_when_add_value_is_greater_than_max_return_max() {
        Gauge gauge = new Gauge(50, 100);

        gauge = gauge.add(100);

        assertThat(gauge.current()).isEqualTo(100);
    }

    @Test
    void should_throw_exception_when_add_value_is_less_than_0() {
        Gauge gauge = new Gauge(50, 100);

        assertThatThrownBy(() -> gauge.add(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Add value must be >= 0");
    }

    @Test
    void should_subtract_value_and_return_new_current_value() {
        Gauge gauge = new Gauge(100, 100);

        gauge = gauge.subtract(50);

        assertThat(gauge.current()).isEqualTo(50);
    }

    @Test
    void should_throw_exception_when_subtract_value_is_less_than_0() {
        Gauge gauge = new Gauge(50, 100);

        assertThatThrownBy(() -> gauge.subtract(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Subtract value must be >= 0");
    }

    @Test
    void should_when_subtract_value_is_less_than_0_return_0() {
        Gauge gauge = new Gauge(50, 100);

        gauge = gauge.subtract(100);

        assertThat(gauge.current()).isZero();
    }

    @Test
    void should_return_true_if_gauge_is_empty() {
        Gauge gauge = new Gauge(0, 100);

        assertThat(gauge.isEmpty()).isTrue();
    }

    @Test
    void should_return_false_if_gauge_is_not_empty() {
        Gauge gauge = new Gauge(1, 100);

        assertThat(gauge.isEmpty()).isFalse();
    }

    @Test
    void should_return_true_if_gauge_is_full() {
        Gauge gauge = new Gauge(100, 100);

        assertThat(gauge.isFull()).isTrue();
    }

    @Test
    void should_return_false_if_gauge_is_not_full() {
        Gauge gauge = new Gauge(1, 100);

        assertThat(gauge.isFull()).isFalse();
    }
}