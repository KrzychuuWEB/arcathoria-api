package com.arcathoria.auth;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AuthenticatedAccountDTO;
import com.arcathoria.account.exception.AccountBadCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {AuthAccountClientAdapter.class})
class AuthAccountClientAdapterTest {

    @MockitoBean
    private AccountQueryFacade accountQueryFacade;

    @Autowired
    private AuthAccountClientAdapter authAccountClientAdapter;

    @Test
    void should_return_correct_account_id_when_valid_email_and_password_and_map_to_account_view() {
        String email = "testemail@arcathoria.com";
        String rawPassword = "password123";
        AuthenticatedAccountDTO authenticatedAccountDTO = new AuthenticatedAccountDTO(UUID.randomUUID(), email);

        when(accountQueryFacade.authenticatedByEmailAndRawPassword(email, rawPassword)).thenReturn(authenticatedAccountDTO);

        AccountView result = authAccountClientAdapter.validate(email, rawPassword);

        assertThat(result.id()).isEqualTo(authenticatedAccountDTO.accountId());
        assertThat(result.email()).isEqualTo(email);

        verify(accountQueryFacade).authenticatedByEmailAndRawPassword(email, rawPassword);
    }

    @Test
    void should_return_bad_request_exception_when_email_or_password_is_incorrect() {
        String email = "testemail@arcathoria.com";
        String rawPassword = "password123";

        when(accountQueryFacade.authenticatedByEmailAndRawPassword(email, rawPassword)).thenThrow(AccountBadCredentialsException.class);

        assertThatThrownBy(() -> authAccountClientAdapter.validate(email, rawPassword))
                .isInstanceOf(AuthBadCredentialsException.class);

        verify(accountQueryFacade).authenticatedByEmailAndRawPassword(email, rawPassword);
    }

    @Test
    void should_return_external_service_unavailable_exception_when_account_service_throws_other_exceptions() {
        String email = "testemail@arcathoria.com";
        String rawPassword = "password123";

        when(accountQueryFacade.authenticatedByEmailAndRawPassword(email, rawPassword)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> authAccountClientAdapter.validate(email, rawPassword))
                .isInstanceOf(ExternalServiceUnavailableException.class);

        verify(accountQueryFacade).authenticatedByEmailAndRawPassword(email, rawPassword);
    }
}