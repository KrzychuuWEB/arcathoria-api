package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;

import java.util.List;

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

    static List<CharacterDTO> toCharacterDTOList(List<Character> characters) {
        return characters.stream()
                .map(character -> new CharacterDTO(
                        character.getSnapshot().getCharacterId().value(),
                        character.getSnapshot().getCharacterName().value()
                ))
                .toList();
    }
}
