package com.arcathoria.character.exception;

import com.arcathoria.ApiException;

public class CharacterAccessDenied extends ApiException {

    public CharacterAccessDenied() {
        super("No access to resource", "ERR_CHARACTER-ACCESS_DENIED");
    }
}
