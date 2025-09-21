package com.arcathoria.combat;

import com.arcathoria.ApiException;
import com.arcathoria.character.CharacterQueryFacade;
import com.arcathoria.character.dto.CharacterDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class CharacterClientAdapter implements CharacterClient {

    private final CharacterQueryFacade characterQueryFacade;

    CharacterClientAdapter(final CharacterQueryFacade characterQueryFacade) {
        this.characterQueryFacade = characterQueryFacade;
    }

    @Override
    public Optional<CharacterDTO> getSelectedCharacterByAccountId(final UUID accountId) {
        try {
            return Optional.of(characterQueryFacade.getSelectedCharacter(accountId));
        } catch (ApiException e) {
            return Optional.empty();
        }
    }
}