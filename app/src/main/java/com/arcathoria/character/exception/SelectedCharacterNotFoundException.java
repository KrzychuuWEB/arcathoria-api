package com.arcathoria.character.exception;

import com.arcathoria.ApiException;

import java.util.UUID;

public class SelectedCharacterNotFoundException extends ApiException {

    private UUID id = null;

    public SelectedCharacterNotFoundException(final UUID id) {
        super("character.get.character.selected.not.found", "ERR_CHARACTER_SELECTED_NOT_FOUND-404");
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
