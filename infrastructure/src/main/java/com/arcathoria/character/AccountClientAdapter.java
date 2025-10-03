package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.exception.CharacterOwnerNotFound;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.stereotype.Component;

@Component
class AccountClientAdapter implements AccountClient {

    private final AccountQueryFacade accountQueryFacade;

    AccountClientAdapter(final AccountQueryFacade accountQueryFacade) {
        this.accountQueryFacade = accountQueryFacade;
    }

    @Override
    public AccountView getById(final AccountId accountId) {
        try {
            return new AccountView(accountQueryFacade.getById(accountId.value()).id());
        } catch (AccountNotFoundException e) {
            throw new CharacterOwnerNotFound(accountId, new UpstreamInfo(e.getDomain(), e.getErrorCode().getCodeName()));
        }
    }
}
