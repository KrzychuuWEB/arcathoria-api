package com.arcathoria.character.vo;

import com.arcathoria.Level;

import java.util.Objects;

public record Intelligence(Level level) implements Attribute<Intelligence> {

    public Intelligence {
        Objects.requireNonNull(level, "Intelligence level cannot be null");
    }

    @Override
    public Intelligence withLevel(final Level level) {
        return new Intelligence(level);
    }
}
