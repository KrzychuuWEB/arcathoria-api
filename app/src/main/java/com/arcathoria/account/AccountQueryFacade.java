package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.vo.AccountId;

import java.util.UUID;

public class AccountQueryFacade {

    private final GetAccountByIdUseCase getAccountByIdUseCase;

    AccountQueryFacade(final GetAccountByIdUseCase getAccountByIdUseCase) {
        this.getAccountByIdUseCase = getAccountByIdUseCase;
    }

    public AccountDTO getById(final UUID uuid) {
        return toDto(getAccountByIdUseCase.execute(new AccountId(uuid)));
    }

    private AccountDTO toDto(final Account account) {
        AccountSnapshot snapshot = account.getSnapshot();
        return new AccountDTO(
                snapshot.getAccountId().value(),
                snapshot.getEmail().value()
        );
    }
}
