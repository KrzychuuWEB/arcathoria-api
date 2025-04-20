package com.arcathoria.monster;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MonsterTest {

    @Test
    void should_return_snapshot_when_restore_aggregate() {
        MonsterSnapshot snapshot = MonsterSnapshotMother.aMonsterSnapshot().build();

        Monster result = Monster.restore(snapshot);

        assertThat(result.getSnapshot().monsterId()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_ID);
        assertThat(result.getSnapshot().monsterName()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_NAME);
        assertThat(result.getSnapshot().health()).isEqualTo(MonsterSnapshotMother.DEFAULT_HEALTH);
    }
}