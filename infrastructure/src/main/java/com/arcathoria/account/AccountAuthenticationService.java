package com.arcathoria.account;

import com.arcathoria.account.vo.Email;
import com.arcathoria.auth.AuthRequestDTO;
import com.arcathoria.auth.JwtTokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AccountAuthenticationService {

    private final AccountQueryRepository accountQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AccountAuthenticationService(
            final AccountQueryRepository accountQueryRepository,
            final PasswordEncoder passwordEncoder,
            final JwtTokenService jwtTokenService
    ) {
        this.accountQueryRepository = accountQueryRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public String authenticate(final AuthRequestDTO authRequestDTO) {
        final Account account = accountQueryRepository.findByEmail(new Email(authRequestDTO.email()))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        final boolean matches = account.getSnapshot().getPassword().matches(authRequestDTO.password(), passwordEncoder);
        if (!matches) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return jwtTokenService.generateToken(
                account.getSnapshot().getEmail().value(),
                account.getSnapshot().getAccountId().value()
        );
    }
}
