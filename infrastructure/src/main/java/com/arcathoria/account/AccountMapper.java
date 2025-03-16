package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;

class AccountMapper {

    AccountMapper() {
    }

    static Account mapToDomain(AccountEntity entity) {
        return Account.restore(
                new AccountSnapshot(
                        new AccountId(entity.getId()),
                        new Email(entity.getEmail()),
                        new HashedPassword(entity.getPassword()
                        )
                ));
    }

    static AccountEntity mapToEntity(Account domain) {
        return new AccountEntity(
                domain.getSnapshot().getAccountId().value(),
                domain.getSnapshot().getEmail().value(),
                domain.getSnapshot().getPassword().getValue()
        );
    }
}
