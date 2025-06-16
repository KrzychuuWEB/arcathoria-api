package com.arcathoria.combat;

import com.arcathoria.combat.vo.Participant;

interface CombatSideStrategy {

    CombatSide choose(final Participant attacker, final Participant defender);
}
