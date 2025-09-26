package com.arcathoria.combat;

import com.arcathoria.combat.exception.CombatAlreadyFinishedException;
import com.arcathoria.combat.exception.ParticipantNotHasActiveCombatsException;
import com.arcathoria.combat.vo.ParticipantId;

class GetActiveCombatByParticipantId {

    private final CombatSessionStore combatSessionStore;

    GetActiveCombatByParticipantId(final CombatSessionStore combatSessionStore) {
        this.combatSessionStore = combatSessionStore;
    }

    Combat getActiveCombat(final ParticipantId participantId) {
        CombatSnapshot snapshot = combatSessionStore.getActiveCombatByParticipantId(participantId)
                .orElseThrow(() -> new ParticipantNotHasActiveCombatsException(participantId.value()));

        Combat combat = Combat.restore(snapshot);

        if (combat.getCombatStatus() != CombatStatus.IN_PROGRESS) {
            throw new CombatAlreadyFinishedException(combat.getSnapshot().combatId());
        }

        return combat;
    }
}
