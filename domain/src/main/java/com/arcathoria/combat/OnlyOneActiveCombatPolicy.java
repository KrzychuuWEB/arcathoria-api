package com.arcathoria.combat;

import com.arcathoria.combat.exception.OnlyOneActiveCombatAllowedDomainException;
import com.arcathoria.combat.vo.ParticipantId;

class OnlyOneActiveCombatPolicy {

    private final CombatSessionStore combatSessionStore;

    OnlyOneActiveCombatPolicy(final CombatSessionStore combatSessionStore) {
        this.combatSessionStore = combatSessionStore;
    }

    void ensureNoneActiveFor(final ParticipantId participantId) {
        if (combatSessionStore.getActiveCombatIdByParticipantId(participantId).isPresent()) {
            throw new OnlyOneActiveCombatAllowedDomainException(participantId);
        }
    }
}
