package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;

import static com.arcathoria.account.AccountDTOMapper.toAccountDTO;

public class AccountFacade {

    private final RegisterUseCase registerUseCase;

    AccountFacade(final RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    public AccountDTO createNewAccount(final RegisterDTO registerDTO) {
        return toAccountDTO(registerUseCase.register(registerDTO));
    }
}
