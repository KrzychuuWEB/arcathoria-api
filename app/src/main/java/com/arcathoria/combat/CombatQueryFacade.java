package com.arcathoria.combat;

import com.arcathoria.combat.dto.CombatIdDTO;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;

import java.util.UUID;

import static com.arcathoria.combat.CombatDTOMapper.fromCombatStateToCombatResultDTO;

public class CombatQueryFacade {

    private final GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId;
    private final CombatParticipantService combatParticipantService;
    private final GetCombatFromStoreByIdAndParticipantId getCombatFromStoreByIdAndParticipantId;

    CombatQueryFacade(final GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId,
                      final CombatParticipantService combatParticipantService,
                      final GetCombatFromStoreByIdAndParticipantId getCombatFromStoreByIdAndParticipantId) {
        this.getActiveCombatIdByParticipantId = getActiveCombatIdByParticipantId;
        this.combatParticipantService = combatParticipantService;
        this.getCombatFromStoreByIdAndParticipantId = getCombatFromStoreByIdAndParticipantId;
    }

    public CombatIdDTO getActiveCombatForSelectedCharacterByAccountId(final UUID accountId) {
        ParticipantId participantId = combatParticipantService.getCharacterByAccountId(new AccountId(accountId)).getId();

        return new CombatIdDTO(getActiveCombatIdByParticipantId.getActiveCombat(participantId).value());
    }

    public CombatResultDTO getCombatByIdAndParticipantId(final UUID combatId, final UUID accountId) {
        ParticipantId participantId = combatParticipantService.getCharacterByAccountId(new AccountId(accountId)).getId();

        return fromCombatStateToCombatResultDTO(getCombatFromStoreByIdAndParticipantId.getByCombatIdAndParticipantId(
                new CombatId(combatId), participantId));
    }
}
