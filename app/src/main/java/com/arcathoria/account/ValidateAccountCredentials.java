package com.arcathoria.account;

import com.arcathoria.account.exception.AccountBadCredentialsException;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class ValidateAccountCredentials {

    private final AccountQueryRepository accountQueryRepository;
    private final PasswordEncoder passwordEncoder;

    ValidateAccountCredentials(final AccountQueryRepository accountQueryRepository, final PasswordEncoder passwordEncoder) {
        this.accountQueryRepository = accountQueryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    Account validate(final Email email, final HashedPassword rawPassword) {
        Account account = accountQueryRepository.findByEmail(email)
                .orElseThrow(AccountBadCredentialsException::new);

        boolean matches = account.getSnapshot().getPassword().matches(rawPassword.getValue(), passwordEncoder);
        if (!matches) {
            throw new AccountBadCredentialsException();
        }

        return account;
    }
}
