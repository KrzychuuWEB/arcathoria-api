package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.exception.CharacterOwnerNotFoundException;
import com.arcathoria.character.exception.ExternalServiceUnavailableException;
import com.arcathoria.character.vo.AccountId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@ContextConfiguration(classes = {AccountClientAdapter.class})
class AccountClientAdapterTest {

    @MockitoBean
    private AccountQueryFacade accountQueryFacade;

    @Autowired
    private AccountClientAdapter accountClientAdapter;

    private final AccountId accountId = new AccountId(UUID.randomUUID());

    @Test
    void should_get_account_by_account_id() {
        AccountId accountId = new AccountId(UUID.randomUUID());

        when(accountQueryFacade.getById(accountId.value())).thenReturn(new AccountDTO(accountId.value(), "test123"));

        AccountView result = accountClientAdapter.getById(accountId);

        assertThat(result.accountId()).isEqualTo(accountId.value());
    }

    @Test
    void should_return_CharacterNotFoundException_when_account_id_not_exists_for_get_by_id() {
        when(accountQueryFacade.getById(accountId.value()))
                .thenThrow(new AccountNotFoundException(new com.arcathoria.account.vo.AccountId(accountId.value())));


        assertThatThrownBy(() -> accountClientAdapter.getById(accountId))
                .isInstanceOf(CharacterOwnerNotFoundException.class)
                .satisfies(e -> {
                    CharacterOwnerNotFoundException ex = (CharacterOwnerNotFoundException) e;
                    assertThat(ex.getUpstreamInfo()).isPresent();
                    assertThat(ex.getUpstreamInfo().get().service()).isEqualTo("account");
                    assertThat(ex.getUpstreamInfo().get().code()).isEqualTo("ERR_ACCOUNT_NOT_FOUND");
                    assertThat(ex.getContext()).containsEntry("accountId", accountId.value());
                });
    }

    @Test
    void should_return_ServiceUnavailable_when_account_query_facade_throws_exception_for_get_by_id() {
        when(accountQueryFacade.getById(accountId.value()))
                .thenThrow(new RuntimeException("test exception"));

        assertThatThrownBy(() -> accountClientAdapter.getById(accountId))
                .isInstanceOf(ExternalServiceUnavailableException.class)
                .satisfies(e -> {
                    ExternalServiceUnavailableException ex = (ExternalServiceUnavailableException) e;
                    assertThat(ex.getContext()).containsEntry("service", "account");
                });
    }
}