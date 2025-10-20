package com.arcathoria.monster;

import com.arcathoria.GenericOpenApiProblemDetail;
import com.arcathoria.monster.exception.MonsterExceptionErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MonsterProblemDetail")
class MonsterOpenApiProblemDetail extends GenericOpenApiProblemDetail<MonsterExceptionErrorCode> {

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(
            description = "Monster service error code",
            implementation = MonsterExceptionErrorCode.class,
            example = "ERR_MONSTER_NOT_FOUND",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public MonsterExceptionErrorCode getErrorCode() {
        return super.getErrorCode();
    }
}
