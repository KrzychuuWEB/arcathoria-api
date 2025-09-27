package com.arcathoria.combat;

import com.arcathoria.character.CharacterQueryFacade;
import com.arcathoria.character.dto.CharacterDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class CharacterClientAdapter implements CharacterClient {

    private final CharacterQueryFacade characterQueryFacade;

    CharacterClientAdapter(final CharacterQueryFacade characterQueryFacade) {
        this.characterQueryFacade = characterQueryFacade;
    }

    @Override
    public CharacterDTO getSelectedCharacterByAccountId(final UUID accountId) {
        return characterQueryFacade.getSelectedCharacter(accountId);
    }
}