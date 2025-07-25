package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.exception.EmailExistsException;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountQueryFacadeTest {

    @Mock
    private GetAccountByIdUseCase getAccountByIdUseCase;

    @Mock
    private CheckEmailExistsUseCase checkEmailExistsUseCase;

    @InjectMocks
    private AccountQueryFacade accountQueryFacade;

    @Test
    void should_get_account_by_id_and_map_to_dto() {
        Account account = Account.restore(AccountSnapshotMother.create().withAccountId(UUID.randomUUID()).build());

        when(getAccountByIdUseCase.execute(any(AccountId.class))).thenReturn(account);

        AccountDTO result = accountQueryFacade.getById(UUID.randomUUID());

        assertThat(result).isInstanceOf(AccountDTO.class);
        assertThat(result.id()).isEqualTo(account.getSnapshot().getAccountId().value());
        assertThat(result.email()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);

        verify(getAccountByIdUseCase).execute(any(AccountId.class));
    }

    @Test
    void should_return_throw_if_email_is_exists() {
        when(checkEmailExistsUseCase.execute(any(Email.class))).thenThrow(EmailExistsException.class);

        assertThatThrownBy(() -> accountQueryFacade.checkWhetherEmailIsExists("used@email.com"))
                .isInstanceOf(EmailExistsException.class);

        verify(checkEmailExistsUseCase).execute(any(Email.class));
    }
}