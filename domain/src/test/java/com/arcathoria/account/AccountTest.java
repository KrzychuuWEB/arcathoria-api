package com.arcathoria.account;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void should_restore_account_from_snapshot() {
        AccountSnapshot snapshot = AccountSnapshotMother.create().build();

        Account account = Account.restore(snapshot);

        assertThat(account).isNotNull();
        assertThat(account.getSnapshot().getAccountId().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_ACCOUNT_ID);
        assertThat(account.getSnapshot().getEmail().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(account.getSnapshot().getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }

    @Test
    void should_return_correct_snapshot() {
        AccountSnapshot snapshot = AccountSnapshotMother.create().build();
        Account account = Account.restore(snapshot);

        AccountSnapshot result = account.getSnapshot();

        assertThat(account).isNotNull();
        assertThat(result.getAccountId().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_ACCOUNT_ID);
        assertThat(result.getEmail().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(result.getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }
}