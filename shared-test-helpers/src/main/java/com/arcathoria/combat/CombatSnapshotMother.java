package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;

import java.util.UUID;

public final class CombatSnapshotMother {
    static final UUID DEFAULT_COMBAT_ID = null;

    private CombatId combatId = new CombatId(DEFAULT_COMBAT_ID);

    static CombatSnapshotMother aCombat() {
        return new CombatSnapshotMother();
    }

    public CombatSnapshotMother withCombatId(final CombatId combatId) {
        this.combatId = combatId;
        return this;
    }

    CombatSnapshot build() {
        return new CombatSnapshot(combatId);
    }
}
