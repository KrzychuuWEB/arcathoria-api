package com.arcathoria.combat;

import com.arcathoria.combat.vo.AccountId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.arcathoria.combat.CombatDTOMapper.fromParticipantViewToParticipant;

class CombatParticipantService {

    private static final Logger log = LogManager.getLogger(CombatParticipantService.class);
    private final CharacterClient characterClient;

    CombatParticipantService(final CharacterClient characterClient) {
        this.characterClient = characterClient;
    }

    Participant getCharacterByAccountId(final AccountId accountId) {
        return fromParticipantViewToParticipant(characterClient.getSelectedCharacterByAccountId(accountId));
    }
}
