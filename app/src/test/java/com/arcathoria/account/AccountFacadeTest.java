package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "secret_password");

        when(registerUseCase.register(any(RegisterDTO.class))).thenReturn(
                Account.restore(
                        new AccountSnapshot(
                                new AccountId(UUID.randomUUID()),
                                new Email(registerDTO.email()),
                                new HashedPassword(registerDTO.password()))
                )
        );

        AccountDTO result = accountFacade.createNewAccount(registerDTO);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(registerDTO.email());

        verify(registerUseCase).register(any(RegisterDTO.class));
    }

    @Test
    void should_throw_exception_when_registration_fails() {
        RegisterDTO registerDTO = new RegisterDTO("taken@email.com", "password");

        when(registerUseCase.register(registerDTO)).thenThrow(new EmailExistsException("Email already in use"));

        assertThatThrownBy(() -> accountFacade.createNewAccount(registerDTO))
                .isInstanceOf(EmailExistsException.class);

        verify(registerUseCase).register(registerDTO);
    }

}