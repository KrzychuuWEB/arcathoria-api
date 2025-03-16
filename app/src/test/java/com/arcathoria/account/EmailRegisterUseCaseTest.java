package com.arcathoria.account;

import com.arcathoria.account.dto.RegisterDTO;
import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailRegisterUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountQueryFacade accountQueryFacade;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountFactory accountFactory;

    @InjectMocks
    private EmailRegisterUseCase registerUseCase;

    @Test
    void should_register_account_when_email_is_not_taken() {
        RegisterDTO registerDTO = new RegisterDTO("good@email.com", "secret_password");
        Account expectedAccount = new AccountFactory().from(new Email(registerDTO.email()), new HashedPassword("hashed_" + registerDTO.password()));

        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("hashed_secret_password");
        when(accountFactory.from(any(Email.class), any(HashedPassword.class))).thenReturn(expectedAccount);

        Account result = registerUseCase.register(registerDTO);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getEmail().value()).isEqualTo(registerDTO.email());
        assertThat(result.getSnapshot().getPassword().getValue()).isEqualTo("hashed_" + registerDTO.password());

        verify(accountRepository).save(any(Account.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void should_throw_exception_when_email_already_exists() {
        RegisterDTO registerDTO = new RegisterDTO("taken@email.com", "secret_password");

        doThrow(new EmailExistsException("email@email.com")).when(accountQueryFacade).checkWhetherEmailIsExists(anyString());

        assertThatThrownBy(() -> registerUseCase.register(registerDTO)).isInstanceOf(EmailExistsException.class);

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void should_use_AccountFactory_to_create_account() {
        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "password");
        Account account = mock(Account.class);

        when(passwordEncoder.encode(registerDTO.password())).thenReturn("hashed_password");
        when(accountFactory.from(any(Email.class), any(HashedPassword.class))).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        registerUseCase.register(registerDTO);

        verify(accountFactory).from(any(Email.class), any(HashedPassword.class));
    }
}