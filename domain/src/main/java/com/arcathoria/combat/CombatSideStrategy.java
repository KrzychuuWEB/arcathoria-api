package com.arcathoria.combat;

interface CombatSideStrategy {

    CombatSide choose(final Participant attacker, final Participant defender);
}
