package com.arcathoria.character;

import com.arcathoria.GenericOpenApiProblemDetail;
import com.arcathoria.character.exception.CharacterExceptionErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CharacterProblemDetail")
class CharacterOpenApiProblemDetail extends GenericOpenApiProblemDetail<CharacterExceptionErrorCode> {

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(
            description = "Character service error code",
            implementation = CharacterExceptionErrorCode.class,
            example = "ERR_CHARACTER_NOT_FOUND",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public CharacterExceptionErrorCode getErrorCode() {
        return super.getErrorCode();
    }
}
