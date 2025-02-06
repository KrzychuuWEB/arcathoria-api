package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;

public class AccountFacade {

    AccountFacade(final RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    private final RegisterUseCase registerUseCase;

    public AccountDTO createNewAccount(RegisterDTO registerDTO) {
        return toDto(registerUseCase.register(registerDTO));
    }

    private AccountDTO toDto(Account account) {
        AccountSnapshot snapshot = account.getSnapshot();
        return new AccountDTO(snapshot.getAccountId().getValue(), snapshot.getEmail().getValue());
    }
}
