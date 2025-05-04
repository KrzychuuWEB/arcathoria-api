package com.arcathoria.character.vo;

public abstract class Resource {

    private Double current;
    private final Double max;

    protected Resource(final Double current, final Double max) {
        if (current < 0 || max < 0) {
            throw new IllegalArgumentException("Resource cannot be less than 0");
        } else if (current > max) {
            throw new IllegalArgumentException("The current value cannot be greater than the maximum");
        }

        this.current = current;
        this.max = max;
    }

    public void add(final Double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Added value cannot be less than 0");
        }

        if ((getCurrent() + value) > max) {
            this.current = max;
        } else {
            this.current = getCurrent() + value;
        }
    }

    public void subtract(final Double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Subtracted value cannot be less than 0");
        }

        if ((getCurrent() - value) < 0) {
            this.current = 0.0;
        } else {
            this.current = this.current - value;
        }
    }

    public Double getMax() {
        return max;
    }

    public Double getCurrent() {
        return current;
    }
}
