package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CombatParticipantService {

    private static final Logger log = LogManager.getLogger(CombatParticipantService.class);
    private final CharacterClient characterClient;

    CombatParticipantService(final CharacterClient characterClient) {
        this.characterClient = characterClient;
    }

    Participant getCharacterByAccountId(final AccountId accountId) {
        return characterClient.getSelectedCharacterByAccountId(accountId.value())
                .map(CombatDTOMapper::fromCharacterDTOToParticipant)
                .orElseThrow(() -> {
                    log.debug("Character not found for id: {}", accountId.value());
                    return new CombatParticipantUnavailableException(accountId.value());
                });
    }
}
