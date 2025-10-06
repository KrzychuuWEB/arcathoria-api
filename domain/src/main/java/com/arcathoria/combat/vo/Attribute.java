package com.arcathoria.combat.vo;

import com.arcathoria.Level;

public sealed interface Attribute<T extends Attribute<T>> permits Intelligence {
    Level level();

    T withLevel(final Level level);

    default T increaseBy(final int delta) {
        return withLevel(level().increaseBy(delta));
    }
}