package com.arcathoria.character.exception;

import com.arcathoria.ApiException;

public class CharacterNameExistsException extends ApiException {

    private final String characterName;

    public CharacterNameExistsException(String characterName) {
        super("character.create.character.name.exists", "ERR_CHARACTER_NAME_EXISTS-409");
        this.characterName = characterName;
    }

    public String getCharacterName() {
        return characterName;
    }
}
