package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.character.dto.AccountView;
import com.arcathoria.character.exception.CharacterOwnerNotFoundException;
import com.arcathoria.character.exception.ExternalServiceUnavailableException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.exception.UpstreamInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
class AccountClientAdapter implements AccountClient {

    private static final Logger log = LogManager.getLogger(AccountClientAdapter.class);

    private final AccountQueryFacade accountQueryFacade;

    AccountClientAdapter(final AccountQueryFacade accountQueryFacade) {
        this.accountQueryFacade = accountQueryFacade;
    }

    @Override
    public AccountView getById(final AccountId accountId) {
        try {
            return new AccountView(accountQueryFacade.getById(accountId.value()).id());
        } catch (AccountNotFoundException e) {
            throw new CharacterOwnerNotFoundException(accountId, new UpstreamInfo(e.getDomain(), e.getErrorCode().getCodeName()));
        } catch (Exception e) {
            log.error("Error getting account for id {}", accountId, e);
            throw new ExternalServiceUnavailableException("account");
        }
    }
}
