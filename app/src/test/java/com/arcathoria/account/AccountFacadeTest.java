package com.arcathoria.account;

import com.arcathoria.account.command.CreateAccountCommand;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.vo.AccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountFacadeTest {

    @Mock
    private RegisterUseCase registerUseCase;

    private AccountFacade accountFacade;

    @BeforeEach
    void setUp() {
        accountFacade = new AccountFacade(registerUseCase);
    }

    @Test
    void should_create_new_account_when_registration_is_successful() {
        CreateAccountCommand command = CreateAccountCommandMother.aCreateAccountCommand().build();
        RegisterDTO registerDTO = new RegisterDTO(command.email().value(), command.password().getValue());

        when(registerUseCase.register(any(CreateAccountCommand.class))).thenReturn(
                Account.restore(
                        new AccountSnapshot(
                                new AccountId(UUID.randomUUID()),
                                command.email(),
                                command.password()
                        )
                ));

        AccountDTO result = accountFacade.createNewAccount(registerDTO);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(registerDTO.email());

        verify(registerUseCase).register(any(CreateAccountCommand.class));
    }
}