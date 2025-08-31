package com.arcathoria.combat;

import com.arcathoria.character.CharacterQueryFacade;
import com.arcathoria.character.dto.CharacterDTO;

import java.util.UUID;

class CharacterClient {

    private final CharacterQueryFacade characterQueryFacade;

    CharacterClient(final CharacterQueryFacade characterQueryFacade) {
        this.characterQueryFacade = characterQueryFacade;
    }

    CharacterDTO getSelectedCharacterByAccountId(final UUID accountId) {
        return characterQueryFacade.getSelectedCharacter(accountId);
    }
}
