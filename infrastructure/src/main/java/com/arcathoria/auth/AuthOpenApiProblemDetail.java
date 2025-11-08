package com.arcathoria.auth;

import com.arcathoria.GenericOpenApiProblemDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthProblemDetail")
class AuthOpenApiProblemDetail extends GenericOpenApiProblemDetail<AuthExceptionErrorCode> {

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(
            description = "Auth service error code",
            implementation = AuthExceptionErrorCode.class,
            example = "ERR_AUTH_BAD_CREDENTIALS",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public AuthExceptionErrorCode getErrorCode() {
        return super.getErrorCode();
    }
}
