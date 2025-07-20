package com.arcathoria.account;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void should_restore_account_from_snapshot() {
        UUID uuid = UUID.randomUUID();
        AccountSnapshot snapshot = AccountSnapshotMother.create().withAccountId(uuid).build();

        Account account = Account.restore(snapshot);

        assertThat(account).isNotNull();
        assertThat(account.getSnapshot().getAccountId().value()).isEqualTo(uuid);
        assertThat(account.getSnapshot().getEmail().value()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(account.getSnapshot().getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }

    @Test
    void should_return_correct_snapshot() {
        UUID uuid = UUID.randomUUID();
        AccountSnapshot snapshot = AccountSnapshotMother.create().withAccountId(uuid).build();
        Account account = Account.restore(snapshot);

        AccountSnapshot result = account.getSnapshot();

        assertThat(account).isNotNull();
        assertThat(result.getAccountId().value()).isEqualTo(uuid);
        assertThat(result.getEmail().value()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(result.getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }
}