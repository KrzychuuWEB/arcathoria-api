package com.arcathoria.character;

import com.arcathoria.character.dto.CreateCharacterDTO;

public final class CreateCharacterDTOMother {
    private String characterName;

    private CreateCharacterDTOMother() {
    }

    public static CreateCharacterDTOMother aCreateCharacterDTO() {
        return new CreateCharacterDTOMother();
    }

    public CreateCharacterDTOMother withCharacterName(String characterName) {
        this.characterName = characterName;
        return this;
    }
    
    public CreateCharacterDTO build() {
        return new CreateCharacterDTO(characterName);
    }
}
