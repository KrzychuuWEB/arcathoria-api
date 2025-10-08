package com.arcathoria.character.exception;

import com.arcathoria.character.vo.CharacterName;

import java.util.Map;

public class CharacterNameExistsException extends CharacterApplicationException {

    private final CharacterName characterName;

    public CharacterNameExistsException(final CharacterName characterName) {
        super("Character name " + characterName.value() + " is exists",
                CharacterExceptionErrorCode.ERR_CHARACTER_NAME_EXISTS,
                Map.of("characterName", characterName.value())
        );
        this.characterName = characterName;
    }

    public CharacterName getCharacterName() {
        return characterName;
    }
}
