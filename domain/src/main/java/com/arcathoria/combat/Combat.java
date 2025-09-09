package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.Damage;

class Combat {

    static Combat restore(final CombatSnapshot snapshot) {
        return new Combat(
                snapshot.combatId(),
                snapshot.attacker(),
                snapshot.defender(),
                snapshot.combatTurn(),
                snapshot.combatType(),
                snapshot.combatStatus()
        );
    }

    private final CombatId combatId;
    private final Participant attacker;
    private final Participant defender;
    private CombatTurn combatTurn;
    private final CombatType combatType;
    private CombatStatus combatStatus;

    Combat(
            final CombatId combatId,
            final Participant attacker,
            final Participant defender,
            final CombatTurn combatTurn,
            final CombatType combatType,
            final CombatStatus combatStatus
    ) {
        this.combatId = combatId;
        this.attacker = attacker;
        this.defender = defender;
        this.combatTurn = combatTurn;
        this.combatType = combatType;
        this.combatStatus = combatStatus;
    }

    CombatSnapshot getSnapshot() {
        return new CombatSnapshot(
                combatId,
                attacker,
                defender,
                combatTurn,
                combatType,
                combatStatus
        );
    }

    CombatSide getCurrentTurn() {
        return combatTurn.currentSide();
    }

    Participant getCurrentTurnParticipant() {
        return getCurrentTurn() == CombatSide.ATTACKER ? attacker : defender;
    }

    void changeTurn() {
        this.combatTurn = combatTurn.changeTurn();
    }

    CombatStatus getCombatStatus() {
        return combatStatus;
    }

    void applyDamageOpponent(final Damage damage) {
        if (getCurrentTurn() == CombatSide.ATTACKER) {
            defender.applyDamage(damage);
        } else {
            attacker.applyDamage(damage);
        }

        if (!isDefenderAlive() || !isAttackerAlive()) {
            this.combatStatus = CombatStatus.FINISHED;
        }
    }

    boolean isDefenderAlive() {
        return defender.isAlive();
    }

    boolean isAttackerAlive() {
        return attacker.isAlive();
    }
}
