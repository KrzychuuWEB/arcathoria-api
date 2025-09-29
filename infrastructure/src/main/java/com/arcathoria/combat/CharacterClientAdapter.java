package com.arcathoria.combat;

import com.arcathoria.character.CharacterQueryFacade;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.vo.AccountId;
import org.springframework.stereotype.Component;

@Component
class CharacterClientAdapter implements CharacterClient {

    private final CharacterQueryFacade characterQueryFacade;

    CharacterClientAdapter(final CharacterQueryFacade characterQueryFacade) {
        this.characterQueryFacade = characterQueryFacade;
    }

    @Override
    public ParticipantView getSelectedCharacterByAccountId(final AccountId accountId) {
        return mapToParticipantView(characterQueryFacade.getSelectedCharacter(accountId.value()));
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