package com.arcathoria.character.vo;

public record Level(int level) {

    public Level {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be greater than 0");
        }
    }

    public Level increaseBy(final int delta) {
        if (delta < 0) throw new IllegalArgumentException("Level delta must be greater than 0");
        int raw = Math.addExact(this.level, delta);
        return new Level(raw);
    }

    public Level next() {
        return increaseBy(1);
    }
}
