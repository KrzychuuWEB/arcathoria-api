package com.arcathoria.character.vo;

public abstract class Attribute {

    private int level;

    protected Attribute(final int level) {
        this.level = cannotLessZero(level);
    }

    public void addLevel(int levelToAdd) {
        this.level = level + cannotLessZero(levelToAdd);
    }

    public int getLevel() {
        return level;
    }

    private int cannotLessZero(final int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("Attribute cannot be less than 0");
        }

        return level;
    }
}
