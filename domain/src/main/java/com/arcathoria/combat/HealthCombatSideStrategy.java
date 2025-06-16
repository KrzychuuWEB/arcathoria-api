package com.arcathoria.combat;

import com.arcathoria.combat.vo.Participant;

class HealthCombatSideStrategy implements CombatSideStrategy {

    @Override
    public CombatSide choose(final Participant attacker, final Participant defender) {
        return attacker.getHealth().getMax() >= defender.getHealth().getMax()
                ? CombatSide.DEFENDER
                : CombatSide.ATTACKER;
    }
}
