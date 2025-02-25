package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class EmailRegisterUseCase implements RegisterUseCase {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountFactory accountFactory;

    EmailRegisterUseCase(final AccountRepository accountRepository, final PasswordEncoder passwordEncoder, final AccountFactory accountFactory) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountFactory = accountFactory;
    }

    @Override
    public Account register(RegisterDTO registerDTO) {
        Email email = new Email(registerDTO.email());

        if (accountRepository.existsByEmail(email)) {
            throw new EmailExistsException("Email " + email.getValue() + " is already used");
        }

        return accountRepository.save(
                accountFactory.from(email, encodePassword(registerDTO.password()))
        );
    }

    private HashedPassword encodePassword(String rawPassword) {
        return HashedPassword.fromRawPassword(rawPassword, passwordEncoder);
    }
}
