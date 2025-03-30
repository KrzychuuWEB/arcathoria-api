package com.arcathoria.character;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SelectCharacterCacheAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private SelectCharacterCacheAdapter characterCacheAdapter;

    @Test
    void should_store_character_id_in_redis() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        CharacterId characterId = new CharacterId(UUID.randomUUID());

        characterCacheAdapter.set(characterId, accountId);

        CharacterId result = characterCacheAdapter.get(accountId);

        assertThat(result).isEqualTo(characterId);
    }
}