package com.arcathoria.account;

import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.account.vo.AccountId;

import java.util.UUID;

class GetAccountByIdUseCase {

    private final AccountRepository accountRepository;

    GetAccountByIdUseCase(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    Account execute(UUID uuid) {
        AccountId accountId = new AccountId(uuid);

        return accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException(accountId.value())
        );
    }
}
