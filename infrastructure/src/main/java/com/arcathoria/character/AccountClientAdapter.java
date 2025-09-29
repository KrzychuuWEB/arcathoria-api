package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.vo.AccountId;
import org.springframework.stereotype.Component;

@Component
class AccountClientAdapter implements AccountClient {

    private final AccountQueryFacade accountQueryFacade;

    AccountClientAdapter(final AccountQueryFacade accountQueryFacade) {
        this.accountQueryFacade = accountQueryFacade;
    }

    @Override
    public AccountView getById(final AccountId accountId) {
        return new AccountView(accountQueryFacade.getById(accountId.value()).id());
    }
}
