package com.arcathoria.combat;

import com.arcathoria.GenericOpenApiProblemDetail;
import com.arcathoria.combat.exception.CombatExceptionErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CombatProblemDetail")
class CombatOpenApiProblemDetail extends GenericOpenApiProblemDetail<CombatExceptionErrorCode> {

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(
            description = "Combat service error code",
            implementation = CombatExceptionErrorCode.class,
            example = "ERR_COMBAT_NOT_FOUND",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public CombatExceptionErrorCode getErrorCode() {
        return super.getErrorCode();
    }
}
