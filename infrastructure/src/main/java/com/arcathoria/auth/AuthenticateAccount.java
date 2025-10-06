package com.arcathoria.auth;

import org.springframework.stereotype.Service;

@Service
class AuthenticateAccount {

    private final AuthAccountClient authAccountClient;
    private final JwtTokenService jwtTokenService;

    AuthenticateAccount(final AuthAccountClient authAccountClient, final JwtTokenService jwtTokenService) {
        this.authAccountClient = authAccountClient;
        this.jwtTokenService = jwtTokenService;
    }

    String authenticate(final AuthRequestDTO authRequestDTO) {
        AccountView accountView = authAccountClient.validate(authRequestDTO.email(), authRequestDTO.password());

        return jwtTokenService.generateToken(accountView.email(), accountView.id());
    }
}
