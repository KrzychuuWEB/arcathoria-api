package com.arcathoria.auth;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AuthenticatedAccountDTO;
import com.arcathoria.account.exception.AccountBadCredentialsException;
import org.springframework.stereotype.Component;

@Component
class AuthAuthAccountClientAdapter implements AuthAccountClient {

    private final AccountQueryFacade accountQueryFacade;

    AuthAuthAccountClientAdapter(final AccountQueryFacade accountQueryFacade) {
        this.accountQueryFacade = accountQueryFacade;
    }

    @Override
    public AccountView validate(final String email, final String password) {
        try {
            return toAuthRequestDTO(accountQueryFacade.authenticatedByEmailAndRawPassword(email, password));
        } catch (AccountBadCredentialsException e) {
            throw new AuthBadCredentialsException();
        } catch (Exception e) {
            throw new ExternalServiceUnavailableException("account");
        }

    }

    private AccountView toAuthRequestDTO(final AuthenticatedAccountDTO authenticatedAccountDTO) {
        return new AccountView(authenticatedAccountDTO.accountId(), authenticatedAccountDTO.email());
    }
}
