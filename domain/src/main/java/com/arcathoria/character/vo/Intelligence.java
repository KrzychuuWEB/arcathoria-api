package com.arcathoria.character.vo;

public record Intelligence(Level level) implements Attribute<Intelligence> {

    @Override
    public Intelligence withLevel(final Level level) {
        return new Intelligence(level);
    }
}
