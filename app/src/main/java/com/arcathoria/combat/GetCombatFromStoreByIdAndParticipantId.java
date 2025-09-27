package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

class GetCombatFromStoreByIdAndParticipantId {

    private final GetCombatSnapshotFromStore getCombatSnapshotFromStore;

    GetCombatFromStoreByIdAndParticipantId(final GetCombatSnapshotFromStore getCombatSnapshotFromStore) {
        this.getCombatSnapshotFromStore = getCombatSnapshotFromStore;
    }

    CombatSnapshot getByCombatIdAndParticipantId(final CombatId combatId, final ParticipantId participantId) {
        CombatSnapshot combatSnapshot = getCombatSnapshotFromStore.getSnapshotById(combatId);

        Combat combat = Combat.restore(combatSnapshot);

        combat.getParticipant(participantId);

        return combatSnapshot;
    }
}
