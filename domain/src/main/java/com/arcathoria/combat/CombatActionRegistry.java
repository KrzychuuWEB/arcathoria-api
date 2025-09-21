package com.arcathoria.combat;

interface CombatActionRegistry {

    CombatAction get(final ActionType actionType);
}
