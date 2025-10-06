package com.arcathoria.auth;

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
    TokenResponseDTO login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return new TokenResponseDTO(authenticateAccount.authenticate(authRequestDTO));
    }
}
