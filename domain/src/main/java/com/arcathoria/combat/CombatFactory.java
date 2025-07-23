package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.Participant;

class CombatFactory {

    Combat createCombat(
            final Participant attacker,
            final Participant defender,
            final CombatSide combatSide,
            final CombatType combatType
    ) {
        return Combat.restore(
                new CombatSnapshot(
                        null,
                        attacker,
                        defender,
                        new CombatTurn(combatSide),
                        combatType
                )
        );
    }
}
