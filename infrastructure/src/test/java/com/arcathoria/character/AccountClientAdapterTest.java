package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.exception.CharacterOwnerNotFound;
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

    @Test
    void should_get_account_by_account_id() {
        AccountId accountId = new AccountId(UUID.randomUUID());

        when(accountQueryFacade.getById(accountId.value())).thenReturn(new AccountDTO(accountId.value(), "test123"));

        AccountView result = accountClientAdapter.getById(accountId);

        assertThat(result.accountId()).isEqualTo(accountId.value());
    }

    @Test
    void should_return_CharacterNotFoundException_when_account_id_not_exists() {
        AccountId accountId = new AccountId(UUID.randomUUID());

        when(accountQueryFacade.getById(accountId.value()))
                .thenThrow(new AccountNotFoundException(new com.arcathoria.account.vo.AccountId(accountId.value())));


        assertThatThrownBy(() -> accountClientAdapter.getById(accountId))
                .isInstanceOf(CharacterOwnerNotFound.class)
                .satisfies(throwable -> {
                    var ex = (CharacterOwnerNotFound) throwable;

                    assertThat(ex.getUpstreamInfo())
                            .isPresent()
                            .get()
                            .satisfies(up -> {
                                assertThat(up.type()).isEqualTo("account");
                                assertThat(up.code()).isEqualTo("ERR_ACCOUNT_NOT_FOUND");
                            });

                    assertThat(ex.getContext()).containsEntry("accountId", accountId.value());
                });
    }
}