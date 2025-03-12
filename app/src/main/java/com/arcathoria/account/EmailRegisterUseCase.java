package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class EmailRegisterUseCase implements RegisterUseCase {

    private static final Logger logger = LogManager.getLogger(EmailRegisterUseCase.class);
    private final AccountRepository accountRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountFactory accountFactory;

    EmailRegisterUseCase(
            final AccountRepository accountRepository,
            final AccountQueryRepository accountQueryRepository,
            final PasswordEncoder passwordEncoder,
            final AccountFactory accountFactory
    ) {
        this.accountRepository = accountRepository;
        this.accountQueryRepository = accountQueryRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountFactory = accountFactory;
    }

    @Override
    public Account register(RegisterDTO registerDTO) {
        Email email = new Email(registerDTO.email());

        if (accountQueryRepository.existsByEmail(email)) {
            throw new EmailExistsException(email.value());
        }

        Account account = accountRepository.save(
                accountFactory.from(email, encodePassword(registerDTO.password()))
        );

        logger.info("Account with email {} has been registered!", email.value());

        return account;
    }

    private HashedPassword encodePassword(String rawPassword) {
        return HashedPassword.fromRawPassword(rawPassword, passwordEncoder);
    }
}
