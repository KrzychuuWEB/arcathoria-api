package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;

import java.util.Optional;

interface AccountRepository {
    boolean existsByEmail(Email email);

    Optional<Account> findByEmail(Email email);

    Optional<Account> findById(AccountId accountId);

    Account save(Account account);
}
