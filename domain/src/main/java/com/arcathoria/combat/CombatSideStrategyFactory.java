package com.arcathoria.combat;

interface CombatSideStrategyFactory {

    CombatSideStrategy getStrategy(final CombatType combatType);
}
