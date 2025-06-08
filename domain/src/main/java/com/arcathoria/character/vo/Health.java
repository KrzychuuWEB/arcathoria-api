package com.arcathoria.character.vo;

public class Health extends Resource {

    public Health(final Double current, final Double max) {
        super(current, max);
    }

    public boolean isAlive() {
        return getCurrent() > 0;
    }
}
