package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void should_restore_account_from_snapshot() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        AccountSnapshot snapshot = new AccountSnapshot(
                accountId,
                new Email("test@email.com"),
                new HashedPassword("secret")
        );

        Account account = Account.restore(snapshot);

        assertThat(account).isNotNull();
        assertThat(account.getSnapshot().getAccountId()).isEqualTo(accountId);
        assertThat(account.getSnapshot().getEmail().getValue()).isEqualTo("test@email.com");
        assertThat(account.getSnapshot().getPassword().getValue()).isEqualTo("secret");
    }

    @Test
    void should_return_correct_snapshot() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        AccountSnapshot snapshot = new AccountSnapshot(
                accountId,
                new Email("test@email.com"),
                new HashedPassword("secret")
        );
        Account account = Account.restore(snapshot);

        AccountSnapshot result = account.getSnapshot();

        assertThat(account).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(accountId);
        assertThat(result.getEmail().getValue()).isEqualTo("test@email.com");
        assertThat(result.getPassword().getValue()).isEqualTo("secret");
    }
}