package com.arcathoria.character;

import com.arcathoria.character.exception.SelectedCharacterNotFoundException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
class SelectCharacterCacheAdapter implements SelectCharacterCachePort {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "active-character:";
    private static final Duration TTL = Duration.ofMinutes(15);

    SelectCharacterCacheAdapter(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setValueAndSetExpiredTime(final CharacterId characterId, final AccountId accountId) {
        String key = getKey(accountId.value());
        redisTemplate.opsForValue().set(key, characterId.value().toString(), TTL);
    }

    @Override
    public CharacterId getAndSetNewExpiredTime(final AccountId accountId) {
        String key = getKey(accountId.value());
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            throw new SelectedCharacterNotFoundException(accountId.value());
        }

        redisTemplate.expire(key, TTL);

        return new CharacterId(UUID.fromString(value));
    }

    @Override
    public void remove(final AccountId accountId) {
        redisTemplate.delete(getKey(accountId.value()));
    }

    private String getKey(final UUID accountId) {
        return KEY_PREFIX + accountId;
    }
}
