package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.monster.vo.Participant;

class Combat {

    static Combat restore(final CombatSnapshot snapshot) {
        return new Combat(
                snapshot.combatId(),
                snapshot.attacker(),
                snapshot.defender()
        );
    }

    private final CombatId combatId;
    private final Participant attacker;
    private final Participant defender;

    Combat(final CombatId combatId, final Participant attacker, final Participant defender) {
        this.combatId = combatId;
        this.attacker = attacker;
        this.defender = defender;
    }

    CombatSnapshot getSnapshot() {
        return new CombatSnapshot(
                combatId,
                attacker,
                defender
        );
    }
}
