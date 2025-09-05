package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CharacterPublicDTO;

import java.util.List;

final class CharacterDTOMapper {

    CharacterDTOMapper() {
    }

    static CharacterDTO toCharacterDTO(Character character) {
        CharacterSnapshot snapshot = character.getSnapshot();
        return new CharacterDTO(
                snapshot.getCharacterId().value(),
                snapshot.getCharacterName().value(),
                snapshot.getHealth().getMax(),
                snapshot.getAttributes().intelligence().level().value()
        );
    }

    static List<CharacterDTO> toCharacterDTOList(List<Character> characters) {
        return characters.stream()
                .map(character -> new CharacterDTO(
                        character.getSnapshot().getCharacterId().value(),
                        character.getSnapshot().getCharacterName().value(),
                        character.getSnapshot().getHealth().getMax(),
                        character.getSnapshot().getAttributes().intelligence().level().value()
                ))
                .toList();
    }

    static CharacterPublicDTO toCharacterPublicDTO(final Character character) {
        return new CharacterPublicDTO(
                character.getSnapshot().getCharacterId().value(),
                character.getSnapshot().getCharacterName().value()
        );
    }
}
