package com.arcathoria.account.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountIdTest {

    @Test
    void should_create_account_id_when_valid_uuid() {
        UUID uuid = UUID.randomUUID();

        AccountId accountId = new AccountId(uuid);

        assertThat(accountId.getId()).isEqualTo(uuid);
    }

    @Test
    void should_throw_exception_when_uuid_is_null() {
        assertThatThrownBy(() -> new AccountId(null)).isInstanceOf(IllegalArgumentException.class);
    }
}