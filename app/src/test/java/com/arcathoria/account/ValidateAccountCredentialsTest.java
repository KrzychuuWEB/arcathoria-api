package com.arcathoria.account;

import com.arcathoria.account.exception.AccountBadCredentialsException;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateAccountCredentialsTest {

    @Mock
    private AccountQueryRepository accountQueryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ValidateAccountCredentials validateAccountCredentials;

    @Test
    void should_return_authenticated_account_by_email_and_password() {
        Email email = new Email("email@arcathoria.com");
        HashedPassword rawPassword = new HashedPassword("password1234");
        Account account = Account.restore(AccountSnapshotMother.create().withEmail(email.value()).withHashedPassword("hashedPassword").build());

        when(accountQueryRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(rawPassword.getValue(), account.getSnapshot().getPassword().getValue())).thenReturn(true);

        Account result = validateAccountCredentials.validate(email, rawPassword);

        assertThat(result).isEqualTo(account);
        assertThat(result.getSnapshot().getEmail()).isEqualTo(email);
        assertThat(result.getSnapshot().getPassword().getValue()).isEqualTo(account.getSnapshot().getPassword().getValue());

        verify(accountQueryRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword.getValue(), account.getSnapshot().getPassword().getValue());
    }

    @Test
    void should_return_bad_credentials_exception_when_account_not_found_with_email() {
        Email email = new Email("email@arcathoria.com");
        HashedPassword rawPassword = new HashedPassword("password1234");

        when(accountQueryRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validateAccountCredentials.validate(email, rawPassword))
                .isInstanceOf(AccountBadCredentialsException.class)
                .hasMessage("Email or password is invalid");

        verify(accountQueryRepository).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void should_return_bad_credentials_exception_when_password_does_not_match() {
        Email email = new Email("email@arcathoria.com");
        HashedPassword rawPassword = new HashedPassword("password1234");
        Account account = Account.restore(AccountSnapshotMother.create().withEmail(email.value()).withHashedPassword("hashedPassword").build());

        when(accountQueryRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(rawPassword.getValue(), account.getSnapshot().getPassword().getValue())).thenReturn(false);

        assertThatThrownBy(() -> validateAccountCredentials.validate(email, rawPassword))
                .isInstanceOf(AccountBadCredentialsException.class)
                .hasMessage("Email or password is invalid");

        verify(accountQueryRepository).findByEmail(email);
        verify(passwordEncoder).matches(anyString(), anyString());
    }
}