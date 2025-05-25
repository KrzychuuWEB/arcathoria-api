package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;

class Combat {

    static Combat restore(final CombatSnapshot snapshot) {
        return new Combat(
                snapshot.combatId()
        );
    }

    private final CombatId combatId;

    Combat(final CombatId combatId) {
        this.combatId = combatId;
    }

    CombatSnapshot getSnapshot() {
        return new CombatSnapshot(
                combatId
        );
    }
}
