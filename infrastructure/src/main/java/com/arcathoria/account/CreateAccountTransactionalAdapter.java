package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.RegisterDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
class CreateAccountTransactionalAdapter {

    private final AccountFacade accountFacade;

    CreateAccountTransactionalAdapter(final AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @Transactional
    AccountDTO transactionalCreateAccount(final RegisterDTO registerDTO) {
        return accountFacade.createNewAccount(registerDTO);
    }
}
