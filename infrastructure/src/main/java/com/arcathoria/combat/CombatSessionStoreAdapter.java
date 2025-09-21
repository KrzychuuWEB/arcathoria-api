package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
class CombatSessionStoreAdapter implements CombatSessionStore {

    private static final Logger log = LogManager.getLogger(CombatSessionStoreAdapter.class);
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final String KEY_PREFIX = "combat:";
    private final Duration TTL = Duration.ofMinutes(15);

    CombatSessionStoreAdapter(final ObjectMapper objectMapper, final RedisTemplate<String, String> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CombatSnapshot save(final CombatSnapshot snapshot) {
        try {
            String key = getKey(snapshot.combatId());
            String json = objectMapper.writeValueAsString(snapshot);
            redisTemplate.opsForValue().set(key, json, TTL);
            return snapshot;
        } catch (JsonProcessingException e) {
            log.error("The fight could not be saved correctly.", e);
            throw new IllegalStateException("Invalid json in redis ", e);
        }
    }

    @Override
    public Optional<CombatSnapshot> getCombatById(final CombatId combatId) {
        String key = getKey(combatId);
        String json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            return Optional.empty();
        }

        try {
            CombatSnapshot snapshot = objectMapper.readValue(json, CombatSnapshot.class);
            redisTemplate.expire(key, TTL);
            return Optional.of(snapshot);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid json in redis ", e);
        }
    }

    private String getKey(CombatId combatId) {
        return KEY_PREFIX + combatId.toString();
    }
}
