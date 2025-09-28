package com.arcathoria;

public record Gauge(int current, int max) {

    public Gauge {
        if (max <= 0) throw new IllegalArgumentException("Max must be > 0");
        if (current < 0) throw new IllegalArgumentException("Current value must be >= 0");
        if (current > max) throw new IllegalArgumentException("Current value must be <= max");
    }

    public Gauge add(final int value) {
        if (value < 0) throw new IllegalArgumentException("Add value must be >= 0");
        return new Gauge(Math.min(current + value, max), max);
    }

    public Gauge subtract(final int value) {
        if (value < 0) throw new IllegalArgumentException("Subtract value must be >= 0");
        return new Gauge(Math.max(current - value, 0), max);
    }

    public boolean isEmpty() {
        return current == 0;
    }

    public boolean isFull() {
        return current == max;
    }
}