package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;

class CreateCharacterUseCase {

    private final CharacterRepository characterRepository;

    CreateCharacterUseCase(final CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    CharacterDTO create(CreateCharacterDTO dto, AccountId accountId) {
        // get account from id

        return null;
    }
}
