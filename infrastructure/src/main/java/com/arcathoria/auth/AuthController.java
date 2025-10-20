package com.arcathoria.auth;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AuthController {

    private final AuthenticateAccount authenticateAccount;

    AuthController(final AuthenticateAccount authenticateAccount) {
        this.authenticateAccount = authenticateAccount;
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "503",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = AuthOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = AuthOpenApiExamples.ACCOUNT_SERVICE_UNAVAILABLE)
            )
    )
    @ApiResponse(responseCode = "401",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = AuthOpenApiProblemDetail.class),
                    examples = {
                            @ExampleObject(
                                    name = "BAD_CREDENTIALS",
                                    value = AuthOpenApiExamples.BAD_CREDENTIALS
                            ),
                            @ExampleObject(
                                    name = "EXPIRED_JWT_TOKEN",
                                    value = AuthOpenApiExamples.EXPIRED_JWT_TOKEN
                            )
                    }
            )
    )
    @ApiResponse(responseCode = "403",
            content = @Content(
                    mediaType = "application/problem+json",
                    schema = @Schema(implementation = AuthOpenApiProblemDetail.class),
                    examples = @ExampleObject(value = AuthOpenApiExamples.ACCESS_DENIED)
            )
    )
    TokenResponseDTO login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return new TokenResponseDTO(authenticateAccount.authenticate(authRequestDTO));
    }
}
