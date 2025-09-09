package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatTurn;

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
                        attacker.getSnapshot(),
                        defender.getSnapshot(),
                        new CombatTurn(combatSide),
                        combatType,
                        CombatStatus.IN_PROGRESS
                )
        );
    }
}
