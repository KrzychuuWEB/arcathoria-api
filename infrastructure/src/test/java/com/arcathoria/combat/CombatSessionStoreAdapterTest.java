package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CombatSessionStoreAdapterTest extends IntegrationTestContainersConfig {

    @Autowired
    private CombatSessionStoreAdapter combatSessionStoreAdapter;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String KEY_PREFIX = "combat:";
    private final String BY_PARTICIPANT_Z_PREFIX = "combat:by-participant:z:";

    @Test
    void should_return_combat_from_session_store_after_save() {
        CombatSnapshot combatSnapshot = CombatSnapshotMother.aCombat().build();

        CombatSnapshot result = combatSessionStoreAdapter.save(combatSnapshot);

        String attackerKey = getKeyByParticipantId(result.attacker().participantId());
        String defenderKey = getKeyByParticipantId(result.defender().participantId());

        assertThat(result).isEqualTo(combatSnapshot);
        assertThat(result.combatStatus()).isEqualTo(combatSnapshot.combatStatus());
        assertThat(result.attacker()).isEqualTo(combatSnapshot.attacker());
        assertThat(result.defender()).isEqualTo(combatSnapshot.defender());
        assertThat(redisTemplate.opsForZSet().score(attackerKey, result.combatId().toString())).isNotNull();
        assertThat(redisTemplate.opsForZSet().score(defenderKey, result.combatId().toString())).isNotNull();
    }

    @Test
    void should_get_combat_by_id_from_session_store() {
        CombatSnapshot combatSnapshot = CombatSnapshotMother.aCombat().build();


        CombatSnapshot save = combatSessionStoreAdapter.save(combatSnapshot);
        Optional<CombatSnapshot> combatById = combatSessionStoreAdapter.getCombatById(save.combatId());
        CombatSnapshot result = combatById.get();

        assertThat(result).isEqualTo(combatSnapshot);
        assertThat(result.combatStatus()).isEqualTo(combatSnapshot.combatStatus());
        assertThat(result.attacker()).isEqualTo(combatSnapshot.attacker());
        assertThat(result.defender()).isEqualTo(combatSnapshot.defender());
    }

    @Test
    void should_refresh_ttl_after_get_combat_by_id() throws InterruptedException {
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().build();
        combatSessionStoreAdapter.save(snapshot);
        String key = KEY_PREFIX + snapshot.combatId();

        Long ttlBefore = redisTemplate.getExpire(key);
        Thread.sleep(200);
        CombatSnapshot getFromStore = combatSessionStoreAdapter.getCombatById(snapshot.combatId()).get();
        Long ttlAfter = redisTemplate.getExpire(key);

        assertThat(getFromStore).isEqualTo(snapshot);
        assertThat(ttlAfter).isNotNull();
        assertThat(ttlBefore).isNotNull();
        assertThat(ttlAfter).isGreaterThan(ttlBefore - 1);
    }

    @Test
    void should_empty_optional_when_combat_is_not_found() {
        Optional<CombatSnapshot> combatById = combatSessionStoreAdapter.getCombatById(new CombatId(UUID.randomUUID()));

        assertThat(combatById).isEmpty();
    }

    @Test
    void should_throw_exception_when_combat_is_not_found_in_redis() {
        CombatId combatId = new CombatId(java.util.UUID.randomUUID());

        redisTemplate.opsForValue().set(KEY_PREFIX + combatId, "not-a-valid-json");

        assertThatThrownBy(() -> combatSessionStoreAdapter.getCombatById(combatId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid json in redis");

    }

    @Test
    void should_remove_combat_from_redis() {
        CombatSnapshot combatSnapshot = CombatSnapshotMother.aCombat().build();

        CombatSnapshot save = combatSessionStoreAdapter.save(combatSnapshot);
        Optional<CombatSnapshot> getBeforeRemove = combatSessionStoreAdapter.getCombatById(save.combatId());
        CombatSnapshot getSnapshot = getBeforeRemove.get();

        String attackerKey = getKeyByParticipantId(getSnapshot.attacker().participantId());
        String defenderKey = getKeyByParticipantId(getSnapshot.defender().participantId());

        combatSessionStoreAdapter.remove(getSnapshot.combatId());
        Optional<CombatSnapshot> getAfterRemove = combatSessionStoreAdapter.getCombatById(save.combatId());

        assertThat(getAfterRemove).isEmpty();
        assertThat(redisTemplate.opsForZSet().score(attackerKey, getSnapshot.combatId().toString())).isNull();
        assertThat(redisTemplate.opsForZSet().score(defenderKey, getSnapshot.combatId().toString())).isNull();
    }

    @Test
    void should_return_active_combat_by_participant_id() {
        CombatSnapshot snapshotToSave = CombatSnapshotMother.aCombat().build();
        combatSessionStoreAdapter.save(snapshotToSave);

        Optional<CombatSnapshot> resultGetByParticipantId = combatSessionStoreAdapter.getActiveCombatByParticipantId(snapshotToSave.attacker().participantId());
        CombatSnapshot snapshot = resultGetByParticipantId.get();

        assertThat(resultGetByParticipantId).isPresent();
        assertThat(snapshot).isEqualTo(snapshotToSave);
        assertThat(snapshot.combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
    }

    @Test
    void should_return_empty_when_combat_already_finished_for_get_combat_by_participant_id() {
        CombatSnapshot snapshotToSave = CombatSnapshotMother.aCombat().withCombatStatus(CombatStatus.FINISHED).build();
        combatSessionStoreAdapter.save(snapshotToSave);

        Optional<CombatSnapshot> resultGetByParticipantId = combatSessionStoreAdapter.getActiveCombatByParticipantId(snapshotToSave.attacker().participantId());

        assertThat(resultGetByParticipantId).isEmpty();
    }

    @Test
    void should_remove_combat_when_combat_status_other_than_combat_in_progress_for_get_combat_by_participant_id() {
        CombatSnapshot snapshotToSave = CombatSnapshotMother.aCombat().withCombatStatus(CombatStatus.FINISHED).build();
        combatSessionStoreAdapter.save(snapshotToSave);

        combatSessionStoreAdapter.getActiveCombatByParticipantId(snapshotToSave.attacker().participantId());

        String attackerKey = getKeyByParticipantId(snapshotToSave.attacker().participantId());
        String defenderKey = getKeyByParticipantId(snapshotToSave.defender().participantId());


        assertThat(redisTemplate.opsForZSet().score(attackerKey, snapshotToSave.combatId().toString())).isNull();
        assertThat(redisTemplate.opsForZSet().score(defenderKey, snapshotToSave.combatId().toString())).isNull();
    }

    private String getKeyByParticipantId(final ParticipantId participantId) {
        return BY_PARTICIPANT_Z_PREFIX + participantId;
    }
}