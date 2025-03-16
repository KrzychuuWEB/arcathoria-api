package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;

final class CharacterDTOMapper {

    CharacterDTOMapper() {
    }

    static CharacterDTO toCharacterDTO(Character character) {
        CharacterSnapshot snapshot = character.getSnapshot();
        return new CharacterDTO(
                snapshot.getCharacterId().value(),
                snapshot.getCharacterName().value()
        );
    }
}
