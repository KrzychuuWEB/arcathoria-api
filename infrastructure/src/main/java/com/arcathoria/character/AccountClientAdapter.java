package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.character.vo.AccountId;
import org.springframework.stereotype.Component;

@Component
class AccountClientAdapter implements AccountClient {

    private final AccountQueryFacade accountQueryFacade;

    AccountClientAdapter(final AccountQueryFacade accountQueryFacade) {
        this.accountQueryFacade = accountQueryFacade;
    }

    @Override
    public AccountDTO getById(final AccountId accountId) {
        return accountQueryFacade.getById(accountId.value());
    }
}
