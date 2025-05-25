package com.arcathoria.combat;

import com.arcathoria.combat.vo.CombatId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CombatTest {

    @Test
    void should_restore_from_snapshot_and_get_values_from_snapshot() {
        CombatId combatId = new CombatId(UUID.randomUUID());

        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).build());

        assertThat(combat.getSnapshot().combatId()).isEqualTo(combatId);
    }
}