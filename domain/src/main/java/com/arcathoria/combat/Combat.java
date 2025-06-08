package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.Participant;

class Combat {

    static Combat restore(final CombatSnapshot snapshot) {
        return new Combat(
                snapshot.combatId(),
                snapshot.attacker(),
                snapshot.defender(),
                snapshot.combatSide()
        );
    }

    private final CombatId combatId;
    private final Participant attacker;
    private final Participant defender;
    private final CombatSide combatSide;

    Combat(
            final CombatId combatId,
            final Participant attacker,
            final Participant defender,
            final CombatSide combatSide
    ) {
        this.combatId = combatId;
        this.attacker = attacker;
        this.defender = defender;
        this.combatSide = combatSide;
    }

    CombatSnapshot getSnapshot() {
        return new CombatSnapshot(
                combatId,
                attacker,
                defender,
                combatSide
        );
    }

    void applyDamageOpponent(final double damage) {
        if (combatSide.equals(CombatSide.ATTACKER)) {
            defender.applyDamage(damage);
        } else {
            attacker.applyDamage(damage);
        }
    }

    boolean isDefenderAlive() {
        return defender.isAlive();
    }

    boolean isAttackerAlive() {
        return attacker.isAlive();
    }
}
