package com.arcathoria.account;

import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.account.vo.AccountId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAccountByIdUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAccountByIdUseCase getAccountByIdUseCase;

    @Test
    void should_get_account_by_id() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        Account account = Account.restore(AccountSnapshotMother.create().withAccountId(accountId.value()).build());

        when(accountRepository.findById(any(AccountId.class))).thenReturn(Optional.of(account));

        Account result = getAccountByIdUseCase.execute(accountId.value());

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(accountId);
        assertThat(result.getSnapshot().getEmail().value()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);

        verify(accountRepository).findById(any(AccountId.class));
    }

    @Test
    void should_get_account_by_id_throw_account_not_found_exception() {
        when(accountRepository.findById(any(AccountId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> getAccountByIdUseCase.execute(UUID.randomUUID())
        ).isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository).findById(any(AccountId.class));
    }
}