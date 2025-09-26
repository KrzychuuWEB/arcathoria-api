package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.UUID;

public class CombatQueryFacade {

    private final GetActiveCombatByParticipantId getActiveCombatByParticipantId;
    private final CombatParticipantService combatParticipantService;

    CombatQueryFacade(final GetActiveCombatByParticipantId getActiveCombatByParticipantId,
                      final CombatParticipantService combatParticipantService) {
        this.getActiveCombatByParticipantId = getActiveCombatByParticipantId;
        this.combatParticipantService = combatParticipantService;
    }

    public UUID getActiveCombatForSelectedCharacterByAccountId(final UUID accountId) {
        ParticipantId participantId = combatParticipantService.getCharacterByAccountId(new AccountId(accountId)).getId();

        return getActiveCombatByParticipantId.getActiveCombat(participantId).getSnapshot().combatId().value();
    }
}
