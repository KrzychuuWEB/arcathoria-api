package com.arcathoria.monster;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MonsterSnapshotTest {

    @Test
    void should_return_MonsterSnapshot() {
        MonsterSnapshot snapshot = MonsterSnapshotMother.aMonsterSnapshot().build();

        assertThat(snapshot.monsterId()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_ID);
        assertThat(snapshot.monsterName()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_NAME);
        assertThat(snapshot.health()).isEqualTo(MonsterSnapshotMother.DEFAULT_HEALTH);
    }
}