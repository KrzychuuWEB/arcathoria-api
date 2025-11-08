package com.arcathoria.character.exception;

import com.arcathoria.character.vo.CharacterId;

import java.util.Map;

public class CharacterNotOwnedException extends CharacterApplicationException {

    private CharacterId characterId;

    public CharacterNotOwnedException(final CharacterId characterId) {
        super("Character with id " + characterId + " is not owned by the authenticated account",
                CharacterExceptionErrorCode.ERR_CHARACTER_NOT_OWNED,
                Map.of("characterId", characterId.value())
        );

        this.characterId = characterId;
    }

    public CharacterId getCharacterId() {
        return characterId;
    }
}
