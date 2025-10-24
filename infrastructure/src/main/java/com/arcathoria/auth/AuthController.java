package com.arcathoria.auth;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
class AuthController {

    private final AuthenticateAccount authenticateAccount;
    private final JwtConfigurationProperties jwtConfigurationProperties;

    AuthController(final AuthenticateAccount authenticateAccount, final JwtConfigurationProperties jwtConfigurationProperties) {
        this.authenticateAccount = authenticateAccount;
        this.jwtConfigurationProperties = jwtConfigurationProperties;
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
    ResponseEntity<Void> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        final String token = authenticateAccount.authenticate(authRequestDTO);

        final ResponseCookie sessionCookie = ResponseCookie
                .from(CookieAndHeaderBearerTokenResolver.SESSION_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(jwtConfigurationProperties.getValidity())
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                .build();
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(final CsrfToken token) {
        return token;
    }
}
