package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;

import java.util.UUID;

class CombatFactory {

    Combat createCombat(
            final Participant attacker,
            final Participant defender,
            final CombatSide combatSide,
            final CombatType combatType
    ) {
        return Combat.restore(
                new CombatSnapshot(
                        new CombatId(UUID.randomUUID()),
                        attacker.getSnapshot(),
                        defender.getSnapshot(),
                        new CombatTurn(combatSide),
                        combatType,
                        CombatStatus.IN_PROGRESS
                )
        );
    }
}
