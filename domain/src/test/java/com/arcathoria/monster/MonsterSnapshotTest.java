package com.arcathoria.monster;

import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MonsterSnapshotTest {

    @Test
    void should_return_MonsterSnapshot() {
        MonsterId id = new MonsterId(UUID.randomUUID());
        MonsterSnapshot snapshot = MonsterSnapshotMother.aMonsterSnapshot().withMonsterId(id).build();

        assertThat(snapshot.monsterId()).isEqualTo(id);
        assertThat(snapshot.monsterName()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_NAME);
        assertThat(snapshot.health()).isEqualTo(MonsterSnapshotMother.DEFAULT_HEALTH);
    }
}