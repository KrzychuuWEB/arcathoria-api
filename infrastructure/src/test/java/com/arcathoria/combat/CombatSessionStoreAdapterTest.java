package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.combat.vo.CombatId;
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

    @Test
    void should_return_combat_from_session_store_after_save() {
        CombatSnapshot combatSnapshot = CombatSnapshotMother.aCombat().build();

        CombatSnapshot result = combatSessionStoreAdapter.save(combatSnapshot);

        assertThat(result).isEqualTo(combatSnapshot);
        assertThat(result.combatStatus()).isEqualTo(combatSnapshot.combatStatus());
        assertThat(result.attacker()).isEqualTo(combatSnapshot.attacker());
        assertThat(result.defender()).isEqualTo(combatSnapshot.defender());
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
    void should_empty_optional_when_combat_is_not_found() {
        Optional<CombatSnapshot> combatById = combatSessionStoreAdapter.getCombatById(new CombatId(UUID.randomUUID()));

        assertThat(combatById).isEmpty();
    }

    @Test
    void should_throw_exception_when_combat_is_not_found_in_redis() {
        CombatId combatId = new CombatId(java.util.UUID.randomUUID());

        redisTemplate.opsForValue().set("combat:" + combatId, "not-a-valid-json");

        assertThatThrownBy(() -> combatSessionStoreAdapter.getCombatById(combatId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid json in redis");

    }
}