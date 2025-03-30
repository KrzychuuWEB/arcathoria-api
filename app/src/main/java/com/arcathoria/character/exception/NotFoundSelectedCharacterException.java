package com.arcathoria.character.exception;

import com.arcathoria.ApiException;

public class NotFoundSelectedCharacterException extends ApiException {

    public NotFoundSelectedCharacterException() {
        super("character.get.character.selected.not.found", "ERR_CHARACTER_SELECTED_NOT_FOUND-404");
    }
}
