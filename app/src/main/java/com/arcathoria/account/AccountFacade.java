package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;

public class AccountFacade {

    private final RegisterUseCase registerUseCase;

    AccountFacade(final RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    public AccountDTO createNewAccount(final RegisterDTO registerDTO) {
        return toDto(registerUseCase.register(registerDTO));
    }

    private AccountDTO toDto(final Account account) {
        AccountSnapshot snapshot = account.getSnapshot();
        return new AccountDTO(snapshot.getAccountId().value(), snapshot.getEmail().value());
    }
}
