package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class EmailRegisterUseCase implements RegisterUseCase {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    EmailRegisterUseCase(final AccountRepository accountRepository, final PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Account register(RegisterDTO registerDTO) {
        Email email = new Email(registerDTO.email());
        if (accountRepository.existsByEmail(email)) {
            throw new EmailExistsException("This email is already used");
        }

        HashedPassword hashedPassword = HashedPassword.fromRawPassword(registerDTO.password(), passwordEncoder);

        return accountRepository.save(Account.restore(new AccountSnapshot(
                null,
                email,
                hashedPassword
        )));
    }
}
