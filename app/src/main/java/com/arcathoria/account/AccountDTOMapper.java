package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.dto.AuthenticatedAccountDTO;

final class AccountDTOMapper {

    AccountDTOMapper() {
    }

    static AccountDTO toAccountDTO(final Account account) {
        AccountSnapshot snapshot = account.getSnapshot();
        return new AccountDTO(snapshot.getAccountId().value(), snapshot.getEmail().value());
    }

    static AuthenticatedAccountDTO toAuthenticatedAccountDTO(final Account account) {
        return new AuthenticatedAccountDTO(account.getSnapshot().getAccountId().value(), account.getSnapshot().getEmail().value());
    }
}
