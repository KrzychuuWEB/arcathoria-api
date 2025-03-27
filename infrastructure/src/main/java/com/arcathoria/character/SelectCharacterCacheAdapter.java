package com.arcathoria.character;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class SelectCharacterCacheAdapter implements SelectCharacterCachePort {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "active-character:";

    SelectCharacterCacheAdapter(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
