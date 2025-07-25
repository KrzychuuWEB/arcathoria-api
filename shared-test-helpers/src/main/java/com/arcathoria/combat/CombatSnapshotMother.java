package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.Participant;
import com.arcathoria.combat.vo.ParticipantMother;

import java.util.UUID;

public final class CombatSnapshotMother {
    static final UUID DEFAULT_COMBAT_ID = null;
    static final Participant DEFAULT_ATTACKER = ParticipantMother.aParticipantBuilder().withHealth(100.0, 100.0).build();
    static final Participant DEFAULT_DEFENDER = ParticipantMother.aParticipantBuilder().withHealth(80.0, 80.0).build();
    static final CombatTurn DEFAULT_COMBAT_TURN = new CombatTurn(CombatSide.ATTACKER);
    static final CombatType DEFAULT_COMBAT_TYPE = CombatType.PVE;

    private CombatId combatId = new CombatId(DEFAULT_COMBAT_ID);
    private Participant attacker = DEFAULT_ATTACKER;
    private Participant defender = DEFAULT_DEFENDER;
    private CombatTurn combatTurn = DEFAULT_COMBAT_TURN;
    private CombatType combatType = DEFAULT_COMBAT_TYPE;

    static CombatSnapshotMother aCombat() {
        return new CombatSnapshotMother();
    }

    CombatSnapshotMother withCombatId(final CombatId combatId) {
        this.combatId = combatId;
        return this;
    }

    CombatSnapshotMother withAttacker(final Participant attacker) {
        this.attacker = attacker;
        return this;
    }

    CombatSnapshotMother withDefender(final Participant defender) {
        this.defender = defender;
        return this;
    }

    CombatSnapshotMother withCombatTurn(final CombatTurn combatTurn) {
        this.combatTurn = combatTurn;
        return this;
    }

    CombatSnapshotMother withCombatType(final CombatType combatType) {
        this.combatType = combatType;
        return this;
    }

    CombatSnapshot build() {
        return new CombatSnapshot(
                combatId,
                attacker,
                defender,
                combatTurn,
                combatType
        );
    }
}
