package com.arcathoria.account;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.account.vo.Email;
import com.arcathoria.account.vo.HashedPassword;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSnapshotTest {

    @Test
    void should_create_account_snapshot_when_valid_data() {
        AccountId uuid = new AccountId(UUID.randomUUID());

        AccountSnapshot snapshot = new AccountSnapshot(
                uuid,
                new Email("test@email.com"),
                new HashedPassword("secret")
        );

        assertThat(snapshot.getAccountId()).isEqualTo(uuid);
        assertThat(snapshot.getEmail().getValue()).isEqualTo("test@email.com");
        assertThat(snapshot.getPassword().getValue()).isEqualTo("secret");
    }
}