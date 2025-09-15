package com.arcathoria.combat;

import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.arcathoria.combat.CombatMapper.fromCharacterDTOToParticipant;

class CombatParticipantService {

    private static final Logger log = LogManager.getLogger(CombatParticipantService.class);
    private final CharacterClient characterClient;

    CombatParticipantService(final CharacterClient characterClient) {
        this.characterClient = characterClient;
    }

    Participant getCharacterByAccountId(final UUID accountId) {
        try {
            return fromCharacterDTOToParticipant(characterClient.getSelectedCharacterByAccountId(accountId));
        } catch (CharacterNotFoundException e) {
            log.warn("Character not found for id: {}", e.getValue());
            throw new CombatParticipantUnavailableException(accountId);
        }
    }
}
