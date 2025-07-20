package com.arcathoria.account;

import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.account.vo.AccountId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class GetAccountByIdUseCase {

    private static final Logger logger = LogManager.getLogger(GetAccountByIdUseCase.class);
    private final AccountQueryRepository accountQueryRepository;

    GetAccountByIdUseCase(final AccountQueryRepository accountQueryRepository) {
        this.accountQueryRepository = accountQueryRepository;
    }

    Account execute(final AccountId accountId) {
        return accountQueryRepository.findById(accountId).orElseThrow(
                () -> {
                    logger.warn("Account not found for ID: {}", accountId.value());
                    return new AccountNotFoundException(accountId.value());
                }
        );
    }
}
