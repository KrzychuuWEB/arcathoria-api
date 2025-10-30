package com.arcathoria.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Auth")
class AuthController {

    private final AuthenticateAccount authenticateAccount;
    private final JwtConfigurationProperties jwtConfigurationProperties;

    AuthController(final AuthenticateAccount authenticateAccount, final JwtConfigurationProperties jwtConfigurationProperties) {
        this.authenticateAccount = authenticateAccount;
        this.jwtConfigurationProperties = jwtConfigurationProperties;
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "login", summary = "Login")
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
        String token = authenticateAccount.authenticate(authRequestDTO);
        ResponseCookie sessionCookie = setHttpOnlyJwtToken(token, jwtConfigurationProperties.getValidity());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                .build();
    }

    @DeleteMapping("/logout")
    @Operation(operationId = "logout", summary = "Logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> logout() {
        ResponseCookie deleteCookie = setHttpOnlyJwtToken("", 0);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @GetMapping("/csrf")
    @Operation(operationId = "csrf", summary = "Get csrf token")
    CsrfToken csrf(final CsrfToken token) {
        return token;
    }

    private ResponseCookie setHttpOnlyJwtToken(final String token, final long maxAge) {
        return ResponseCookie.from(CookieAndHeaderBearerTokenResolver.SESSION_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(maxAge)
                .partitioned(true)
                .build();
    }
}
