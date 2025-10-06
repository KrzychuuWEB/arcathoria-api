package com.arcathoria.account;

import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CheckEmailExistsUseCase {

    private static final Logger log = LogManager.getLogger(CheckEmailExistsUseCase.class);
    private final AccountQueryRepository accountQueryRepository;

    CheckEmailExistsUseCase(final AccountQueryRepository accountQueryRepository) {
        this.accountQueryRepository = accountQueryRepository;
    }

    boolean execute(final Email email) {
        if (accountQueryRepository.existsByEmail(email)) {
            log.warn("Email has been exists {}", email);
            throw new EmailExistsException(email);
        }

        return false;
    }
}
