package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class CreateCharacterTransactionalAdapter {

    private final CharacterFacade characterFacade;

    CreateCharacterTransactionalAdapter(final CharacterFacade characterFacade) {
        this.characterFacade = characterFacade;
    }

    @Transactional
    CharacterDTO transactionalCreateCharacter(final CreateCharacterDTO createCharacterDTO, final UUID accountId) {
        return characterFacade.createCharacter(createCharacterDTO, accountId);
    }
}
