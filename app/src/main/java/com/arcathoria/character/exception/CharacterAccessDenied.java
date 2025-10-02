package com.arcathoria.character.exception;

import com.arcathoria.character.vo.CharacterId;

import java.util.Map;

public class CharacterAccessDenied extends CharacterApplicationException {

    private CharacterId characterId;

    public CharacterAccessDenied(final CharacterId characterId) {
        super("Character with id " + characterId.value() + " is not have access to this operation",
                CharacterExceptionErrorCode.ERR_CHARACTER_ACCESS_DENIED,
                Map.of("characterId", characterId.value())
        );

        this.characterId = characterId;
    }

    public CharacterId getCharacterId() {
        return characterId;
    }
}
