package com.arcathoria.account;

import com.arcathoria.account.command.CreateAccountCommand;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

import static com.arcathoria.account.AccountDTOMapper.toAccountDTO;

public class AccountFacade {

    private final RegisterUseCase registerUseCase;

    AccountFacade(final RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    public AccountDTO createNewAccount(final RegisterDTO registerDTO) {
        CreateAccountCommand command = new CreateAccountCommand(
                new Email(registerDTO.email()),
                new HashedPassword(registerDTO.password())
        );

        return toAccountDTO(registerUseCase.register(command));
    }
}
