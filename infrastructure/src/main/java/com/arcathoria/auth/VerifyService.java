package com.arcathoria.auth;

import com.arcathoria.account.MyUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
class VerifyService {

    private final JwtTokenService jwtTokenService;

    private final AuthenticationManager authenticationManager;

    VerifyService(final JwtTokenService jwtTokenService, final AuthenticationManager authenticationManager) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
    }

    public String verify(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.email(), authRequestDTO.password()));

        var user = (MyUserDetails) authentication.getPrincipal();
        return jwtTokenService.generateToken(user.getUsername(), user.getId());
    }
}
