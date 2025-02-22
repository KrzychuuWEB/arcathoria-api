package com.arcathoria.account;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountSnapshotTest {

    @Test
    void should_create_account_snapshot_when_valid_data() {
        UUID uuid = UUID.randomUUID();
        AccountSnapshot snapshot = AccountSnapshotMother.create().withAccountId(uuid).build();

        assertThat(snapshot.getAccountId().getValue()).isEqualTo(uuid);
        assertThat(snapshot.getEmail().getValue()).isEqualTo("default@email.com");
        assertThat(snapshot.getPassword().getValue()).isEqualTo("secret");
    }
}