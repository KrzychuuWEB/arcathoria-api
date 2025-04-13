package com.arcathoria.character.exception;

import com.arcathoria.ApiException;

public class CharacterNotFoundException extends ApiException {

    private final String value;

    public CharacterNotFoundException(String value) {
        super("Character not found for value: " + value, "ERR_CHARACTER_NOT_FOUND-404");
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
