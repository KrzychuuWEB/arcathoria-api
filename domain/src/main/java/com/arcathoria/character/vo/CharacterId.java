package com.arcathoria.character.vo;

import java.util.Objects;
import java.util.UUID;

public class CharacterId {

    private final UUID value;

    public CharacterId(final UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final CharacterId that = (CharacterId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
