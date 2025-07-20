package com.arcathoria.combat;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CombatSnapshotTest {

    @Test
    void should_return_default_values_from_combat_snapshot_mother() {
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().build();

        assertThat(snapshot.combatId().value()).isEqualTo(CombatSnapshotMother.DEFAULT_COMBAT_ID);
    }
}
