package com.arcathoria.combat;

import com.arcathoria.character.CharacterQueryFacade;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.exception.CharacterNotSelectedException;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
class CharacterClientAdapter implements CharacterClient {


    private static final Logger log = LogManager.getLogger(CharacterClientAdapter.class);
    private final CharacterQueryFacade characterQueryFacade;

    CharacterClientAdapter(final CharacterQueryFacade characterQueryFacade) {
        this.characterQueryFacade = characterQueryFacade;
    }

    @Override
    public ParticipantView getSelectedCharacterByAccountId(final AccountId accountId) {
        try {
            return mapToParticipantView(characterQueryFacade.getSelectedCharacter(accountId.value()));
        } catch (CharacterNotSelectedException e) {
            throw new CombatParticipantNotAvailableException(new ParticipantId(e.getAccountId().value()),
                    new UpstreamInfo(e.getDomain(), e.getErrorCode().getCodeName()));
        } catch (CharacterNotFoundException e) {
            throw new CombatParticipantNotAvailableException(new ParticipantId(e.getCharacterId().value()),
                    new UpstreamInfo(e.getDomain(), e.getErrorCode().getCodeName()));
        } catch (Exception e) {
            log.error("Error getting character for account id {}", accountId, e);
            throw new ExternalServiceUnavailableException("character");
        }
    }

    private ParticipantView mapToParticipantView(final CharacterDTO characterDTO) {
        return new ParticipantView(
                characterDTO.id(),
                characterDTO.characterName(),
                characterDTO.health(),
                characterDTO.intelligence(),
                ParticipantType.PLAYER
        );
    }
}