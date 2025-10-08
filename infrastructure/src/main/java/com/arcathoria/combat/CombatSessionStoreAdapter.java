package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Component
class CombatSessionStoreAdapter implements CombatSessionStore {

    private static final Logger log = LogManager.getLogger(CombatSessionStoreAdapter.class);

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private final String KEY_PREFIX = "combat:";
    private final String BY_PARTICIPANT_Z_PREFIX = "combat:by-participant:z:";
    private final Duration TTL = Duration.ofMinutes(15);
    private final int ZSET_CHUNK = 10;

    CombatSessionStoreAdapter(final ObjectMapper objectMapper, final RedisTemplate<String, String> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CombatSnapshot save(final CombatSnapshot snapshot) {
        try {
            String combatKey = getCombatKey(snapshot.combatId());
            String json = objectMapper.writeValueAsString(snapshot);

            redisTemplate.opsForValue().set(combatKey, json, TTL);
            long now = System.currentTimeMillis();
            String attackerZ = getParticipantZKey(snapshot.attacker().participantId());
            String defenderZ = getParticipantZKey(snapshot.defender().participantId());
            String combatId = snapshot.combatId().toString();

            redisTemplate.opsForZSet().add(attackerZ, combatId, now);
            redisTemplate.opsForZSet().add(defenderZ, combatId, now);

            redisTemplate.expire(attackerZ, TTL);
            redisTemplate.expire(defenderZ, TTL);

            return snapshot;
        } catch (JsonProcessingException e) {
            log.error("The fight could not be saved correctly.", e);
            throw new IllegalStateException("Invalid json in redis ", e);
        }
    }

    @Override
    public Optional<CombatSnapshot> getCombatById(final CombatId combatId) {
        String key = getCombatKey(combatId);
        String json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            return Optional.empty();
        }

        try {
            CombatSnapshot snapshot = objectMapper.readValue(json, CombatSnapshot.class);
            touch(snapshot);
            return Optional.of(snapshot);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid json in redis ", e);
        }
    }

    @Override
    public void remove(final CombatId combatId) {
        final String cKey = getCombatKey(combatId);
        String json = redisTemplate.opsForValue().get(cKey);

        if (json != null) {
            try {
                CombatSnapshot s = objectMapper.readValue(json, CombatSnapshot.class);

                String attackerZ = getParticipantZKey(s.attacker().participantId());
                String defenderZ = getParticipantZKey(s.defender().participantId());
                String cid = combatId.toString();

                redisTemplate.opsForZSet().remove(attackerZ, cid);
                redisTemplate.opsForZSet().remove(defenderZ, cid);
            } catch (JsonProcessingException e) {
                log.warn("Invalid snapshot json during remove for {}", combatId, e);
            }
        }

        redisTemplate.delete(cKey);
        log.debug("Removed combat {} (and zset entries if snapshot was present).", combatId);
    }

    @Override
    public Optional<CombatId> getActiveCombatIdByParticipantId(final ParticipantId participantId) {
        String zKey = getParticipantZKey(participantId);
        int start = 0, end = ZSET_CHUNK - 1;

        while (true) {
            Set<String> ids = redisTemplate.opsForZSet().reverseRange(zKey, start, end);
            if (ids == null || ids.isEmpty()) return Optional.empty();

            Optional<CombatSnapshot> active = findActiveInBatch(ids, participantId, zKey);
            if (active.isPresent()) return Optional.ofNullable(active.get().combatId());

            start += ZSET_CHUNK;
            end += ZSET_CHUNK;
        }
    }

    private Optional<CombatSnapshot> findActiveInBatch(final Set<String> ids, final ParticipantId participantId, final String zKey) {
        for (String cid : ids) {
            String json = redisTemplate.opsForValue().get(getCombatKey(cid));
            if (json == null) {
                redisTemplate.opsForZSet().remove(zKey, cid);
                continue;
            }

            Optional<CombatSnapshot> snapshot = parseAndValidate(json, cid, participantId, zKey);
            if (snapshot.isPresent()) return snapshot;
        }
        return Optional.empty();
    }

    private Optional<CombatSnapshot> parseAndValidate(final String json, final String cid, final ParticipantId pid, final String zKey) {
        try {
            CombatSnapshot snapshot = objectMapper.readValue(json, CombatSnapshot.class);
            if (!belongsTo(snapshot, pid)) {
                redisTemplate.opsForZSet().remove(zKey, cid);
                return Optional.empty();
            }
            if (snapshot.combatStatus() != CombatStatus.IN_PROGRESS) {
                redisTemplate.opsForZSet().remove(zKey, cid);
                return Optional.empty();
            }
            touch(snapshot);
            return Optional.of(snapshot);
        } catch (Exception e) {
            redisTemplate.opsForZSet().remove(zKey, cid);
            return Optional.empty();
        }
    }

    private boolean belongsTo(final CombatSnapshot s, ParticipantId pid) {
        return s.attacker().participantId().equals(pid) || s.defender().participantId().equals(pid);
    }

    private String getCombatKey(final CombatId combatId) {
        return KEY_PREFIX + combatId.toString();
    }

    private String getCombatKey(String combatId) {
        return "combat:" + combatId;
    }

    private String getParticipantZKey(final ParticipantId participantId) {
        return BY_PARTICIPANT_Z_PREFIX + participantId.toString();
    }

    private void touch(CombatSnapshot s) {
        String cKey = getCombatKey(s.combatId());
        redisTemplate.expire(cKey, TTL);

        redisTemplate.expire(getParticipantZKey(s.attacker().participantId()), TTL);
        redisTemplate.expire(getParticipantZKey(s.defender().participantId()), TTL);
    }

}
