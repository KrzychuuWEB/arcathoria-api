package com.arcathoria.account;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSnapshotTest {

    @Test
    void should_create_account_snapshot_when_valid_data() {
        UUID uuid = UUID.randomUUID();
        AccountSnapshot snapshot = AccountSnapshotMother.create().withAccountId(uuid).build();

        assertThat(snapshot.getAccountId().value()).isEqualTo(uuid);
        assertThat(snapshot.getEmail().value()).isEqualTo(AccountSnapshotMother.DEFAULT_EMAIL);
        assertThat(snapshot.getPassword().getValue()).isEqualTo(AccountSnapshotMother.DEFAULT_HASHED_PASSWORD);
    }
}