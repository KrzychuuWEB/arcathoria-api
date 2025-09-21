package com.arcathoria.character.vo;

import java.util.Objects;

public record Health(Gauge gauge) {

    public Health {
        Objects.requireNonNull(gauge, "Health gauge cannot be null");
    }

    public Health add(final int value) {
        return new Health(gauge.add(value));
    }

    public Health subtract(final int value) {
        return new Health(gauge.subtract(value));
    }

    public boolean isAlive() {
        return !gauge.isEmpty();
    }

    public int getCurrent() {
        return gauge.current();
    }

    public int getMax() {
        return gauge.max();
    }
}
