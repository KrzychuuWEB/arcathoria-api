package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.arcathoria.combat.CombatMapper.fromCharacterDTOToParticipant;

class CombatParticipantService {

    private static final Logger log = LogManager.getLogger(CombatParticipantService.class);
    private final CharacterClient characterClient;

    CombatParticipantService(final CharacterClient characterClient) {
        this.characterClient = characterClient;
    }

    Participant getCharacterByAccountId(final AccountId accountId) {
        try {
            return fromCharacterDTOToParticipant(characterClient.getSelectedCharacterByAccountId(accountId.value()));
        } catch (CharacterNotFoundException e) {
            log.warn("Character not found for id: {}", e.getValue());
            throw new CombatParticipantUnavailableException(accountId.value());
        }
    }
}
