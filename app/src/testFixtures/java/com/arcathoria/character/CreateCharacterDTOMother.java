package com.arcathoria.character;

import com.arcathoria.UUIDGenerator;
import com.arcathoria.character.dto.CreateCharacterDTO;

final class CreateCharacterDTOMother {
    private String characterName = "char_" + UUIDGenerator.generate(10);

    private CreateCharacterDTOMother() {
    }

    static CreateCharacterDTOMother aCreateCharacterDTO() {
        return new CreateCharacterDTOMother();
    }

    CreateCharacterDTOMother withCharacterName(String characterName) {
        this.characterName = characterName;
        return this;
    }

    CreateCharacterDTO build() {
        return new CreateCharacterDTO(characterName);
    }
}
