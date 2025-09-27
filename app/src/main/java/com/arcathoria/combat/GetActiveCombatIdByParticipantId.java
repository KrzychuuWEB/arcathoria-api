package com.arcathoria.combat;

import com.arcathoria.combat.exception.ParticipantNotHasActiveCombatsException;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

class GetActiveCombatIdByParticipantId {

    private final CombatSessionStore combatSessionStore;

    GetActiveCombatIdByParticipantId(final CombatSessionStore combatSessionStore) {
        this.combatSessionStore = combatSessionStore;
    }

    CombatId getActiveCombat(final ParticipantId participantId) {
        return combatSessionStore.getActiveCombatIdByParticipantId(participantId)
                .orElseThrow(() -> new ParticipantNotHasActiveCombatsException(participantId.value()));
    }
}
