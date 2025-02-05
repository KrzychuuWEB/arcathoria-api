package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.vo.Email;

class EmailRegisterUseCase implements RegisterUseCase {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    EmailRegisterUseCase(final AccountRepository accountRepository, final PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account register(RegisterDTO registerDTO) {
        if (accountRepository.existsByEmail(new Email(registerDTO.email()))) {
            return null;
        }

        return null;
    }
}
