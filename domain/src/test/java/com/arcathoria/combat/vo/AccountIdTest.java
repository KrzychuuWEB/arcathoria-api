package com.arcathoria.combat.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountIdTest {

    @Test
    void should_create_account_id_when_valid_uuid() {
        UUID uuid = UUID.randomUUID();

        AccountId accountId = new AccountId(uuid);

        assertThat(accountId.value()).isEqualTo(uuid);
    }
}