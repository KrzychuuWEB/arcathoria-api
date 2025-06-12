package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.Participant;

class Combat {

    static Combat restore(final CombatSnapshot snapshot) {
        return new Combat(
                snapshot.combatId(),
                snapshot.attacker(),
                snapshot.defender(),
                snapshot.combatTurn(),
                snapshot.combatType()
        );
    }

    private final CombatId combatId;
    private final Participant attacker;
    private final Participant defender;
    private final CombatTurn combatTurn;
    private final CombatType combatType;

    Combat(
            final CombatId combatId,
            final Participant attacker,
            final Participant defender,
            final CombatTurn combatTurn,
            final CombatType combatType
    ) {
        this.combatId = combatId;
        this.attacker = attacker;
        this.defender = defender;
        this.combatTurn = combatTurn;
        this.combatType = combatType;
    }

    CombatSnapshot getSnapshot() {
        return new CombatSnapshot(
                combatId,
                attacker,
                defender,
                combatTurn,
                combatType
        );
    }

    CombatSide getCurrentTurn() {
        return combatTurn.getCurrent();
    }

    void changeTurn() {
        combatTurn.changeTurn();
    }

    void applyDamageOpponent(final double damage) {
        if (combatTurn.getCurrent().equals(CombatSide.ATTACKER)) {
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
