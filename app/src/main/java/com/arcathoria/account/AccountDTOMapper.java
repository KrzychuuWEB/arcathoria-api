package com.arcathoria.account;

import com.arcathoria.account.dto.AccountDTO;

final class AccountDTOMapper {

    AccountDTOMapper() {
    }

    static AccountDTO toAccountDTO(Account account) {
        AccountSnapshot snapshot = account.getSnapshot();
        return new AccountDTO(snapshot.getAccountId().value(), snapshot.getEmail().value());
    }
}
