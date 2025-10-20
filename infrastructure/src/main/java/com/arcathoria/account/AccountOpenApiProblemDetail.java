package com.arcathoria.account;

import com.arcathoria.GenericOpenApiProblemDetail;
import com.arcathoria.account.exception.AccountExceptionErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountProblemDetail")
class AccountOpenApiProblemDetail extends GenericOpenApiProblemDetail<AccountExceptionErrorCode> {

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(
            description = "Account service error code",
            implementation = AccountExceptionErrorCode.class,
            example = "ERR_ACCOUNT_NOT_FOUND",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public AccountExceptionErrorCode getErrorCode() {
        return super.getErrorCode();
    }
}
