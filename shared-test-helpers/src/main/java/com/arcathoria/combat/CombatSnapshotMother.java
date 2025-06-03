package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.Participant;
import com.arcathoria.combat.vo.ParticipantMother;

import java.util.UUID;

public final class CombatSnapshotMother {
    static final UUID DEFAULT_COMBAT_ID = null;

    private CombatId combatId = new CombatId(DEFAULT_COMBAT_ID);
    private Participant attacker = ParticipantMother.aParticipantBuilder().build();
    private Participant defender = ParticipantMother.aParticipantBuilder().build();

    static CombatSnapshotMother aCombat() {
        return new CombatSnapshotMother();
    }

    public CombatSnapshotMother withCombatId(final CombatId combatId) {
        this.combatId = combatId;
        return this;
    }

    public CombatSnapshotMother withAttacker(final Participant attacker) {
        this.attacker = attacker;
        return this;
    }

    public CombatSnapshotMother withDefender(final Participant defender) {
        this.defender = defender;
        return this;
    }

    CombatSnapshot build() {
        return new CombatSnapshot(combatId, attacker, defender);
    }
}
