package com.arcathoria.character.exception;

import com.arcathoria.character.vo.CharacterId;

import java.util.Map;

public class CharacterNotFoundException extends CharacterApplicationException {

    private final CharacterId characterId;

    public CharacterNotFoundException(final CharacterId characterId) {
        super("Character with id " + characterId.value() + " not found!",
                CharacterExceptionErrorCode.ERR_CHARACTER_NOT_FOUND,
                Map.of("characterId", characterId.value())
        );
        this.characterId = characterId;
    }

    public CharacterId getCharacterId() {
        return characterId;
    }
}
