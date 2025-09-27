package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.UUID;

public class CombatQueryFacade {

    private final GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId;
    private final CombatParticipantService combatParticipantService;

    CombatQueryFacade(final GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId,
                      final CombatParticipantService combatParticipantService) {
        this.getActiveCombatIdByParticipantId = getActiveCombatIdByParticipantId;
        this.combatParticipantService = combatParticipantService;
    }

    public UUID getActiveCombatForSelectedCharacterByAccountId(final UUID accountId) {
        ParticipantId participantId = combatParticipantService.getCharacterByAccountId(new AccountId(accountId)).getId();

        return getActiveCombatIdByParticipantId.getActiveCombat(participantId).value();
    }
}
