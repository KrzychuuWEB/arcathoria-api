package com.arcathoria.auth;

import com.arcathoria.account.AccountAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AuthController {

    private final AccountAuthenticationService accountAuthenticationService;

    AuthController(final AccountAuthenticationService accountAuthenticationService) {
        this.accountAuthenticationService = accountAuthenticationService;
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    TokenResponseDTO login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return new TokenResponseDTO(accountAuthenticationService.authenticate(authRequestDTO));
    }
}
