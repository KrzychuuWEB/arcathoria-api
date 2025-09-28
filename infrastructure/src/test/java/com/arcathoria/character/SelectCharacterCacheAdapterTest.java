package com.arcathoria.character;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.character.exception.SelectedCharacterNotFoundException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SelectCharacterCacheAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private SelectCharacterCacheAdapter characterCacheAdapter;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private AccountId accountId;
    private CharacterId characterId;

    @BeforeEach
    void setValueAndSetExpiredTimeUp() {
        accountId = new AccountId(UUID.randomUUID());
        characterId = new CharacterId(UUID.randomUUID());
    }

    @Test
    void should_store_character_id_in_redis() {
        characterCacheAdapter.setValueAndSetExpiredTime(this.characterId, this.accountId);

        CharacterId result = characterCacheAdapter.getAndSetNewExpiredTime(this.accountId);

        assertThat(result).isEqualTo(characterId);
    }

    @Test
    void should_extend_ttl_when_character_id_is_retrieved() {
        characterCacheAdapter.setValueAndSetExpiredTime(characterId, accountId);

        sleep(1500);

        String redisKey = "active-character:" + this.accountId.value();
        Long ttlBefore = redisTemplate.getExpire(redisKey);

        characterCacheAdapter.getAndSetNewExpiredTime(accountId);

        Long ttlAfter = redisTemplate.getExpire(redisKey);

        assertThat(ttlBefore).isNotNull();
        assertThat(ttlAfter).isNotNull();
        assertThat(ttlBefore).isLessThan(ttlAfter);
    }

    @Test
    void should_remove_character_id_from_redis() {
        characterCacheAdapter.setValueAndSetExpiredTime(this.characterId, this.accountId);
        characterCacheAdapter.remove(this.accountId);

        assertThatThrownBy(() -> characterCacheAdapter.getAndSetNewExpiredTime(this.accountId))
                .isInstanceOf(SelectedCharacterNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_character_id_is_not_in_redis() {
        assertThatThrownBy(() -> characterCacheAdapter.getAndSetNewExpiredTime(this.accountId))
                .isInstanceOf(SelectedCharacterNotFoundException.class);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}