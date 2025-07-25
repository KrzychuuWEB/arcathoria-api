package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class AccountFactory {

    Account from(final Email email, final HashedPassword hashedPassword) {
        return Account.restore(new AccountSnapshot(
                new AccountId(null),
                email,
                hashedPassword
        ));
    }
}
