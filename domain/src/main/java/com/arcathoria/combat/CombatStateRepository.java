package com.arcathoria.combat;

interface CombatStateRepository {
    CombatSnapshot save(final CombatSnapshot snapshot);
}
