package com.arcathoria.account;

import com.arcathoria.account.command.CreateAccountCommand;
import com.arcathoria.account.vo.HashedPassword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class EmailRegisterUseCase implements RegisterUseCase {

    private static final Logger logger = LogManager.getLogger(EmailRegisterUseCase.class);
    private final AccountRepository accountRepository;
    private final AccountQueryFacade accountQueryFacade;
    private final PasswordEncoder passwordEncoder;
    private final AccountFactory accountFactory;

    EmailRegisterUseCase(
            final AccountRepository accountRepository,
            final AccountQueryFacade accountQueryFacade,
            final PasswordEncoder passwordEncoder,
            final AccountFactory accountFactory
    ) {
        this.accountRepository = accountRepository;
        this.accountQueryFacade = accountQueryFacade;
        this.passwordEncoder = passwordEncoder;
        this.accountFactory = accountFactory;
    }

    @Override
    public Account register(final CreateAccountCommand command) {
        accountQueryFacade.checkWhetherEmailIsExists(command.email().value());

        Account account = accountRepository.save(
                accountFactory.from(command.email(), encodePassword(command.password().getValue()))
        );

        logger.info("Account with email {} has been registered!", command.email().value());

        return account;
    }

    private HashedPassword encodePassword(final String rawPassword) {
        return HashedPassword.fromRawPassword(rawPassword, passwordEncoder);
    }
}
