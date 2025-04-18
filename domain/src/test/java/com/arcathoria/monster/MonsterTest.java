package com.arcathoria.monster;

import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MonsterTest {

    @Test
    void should_return_snapshot_when_restore_aggregate() {
        MonsterId monsterId = new MonsterId(UUID.randomUUID());
        MonsterSnapshot snapshot = MonsterSnapshotMother.aMonsterSnapshot().withMonsterId(monsterId).build();

        Monster result = Monster.restore(snapshot);

        assertThat(result.getSnapshot().getMonsterId()).isEqualTo(monsterId);
        assertThat(result.getSnapshot().getMonsterName()).isEqualTo(MonsterSnapshotMother.DEFAULT_MONSTER_NAME);
        assertThat(result.getSnapshot().getHealth()).isEqualTo(MonsterSnapshotMother.DEFAULT_HEALTH);
    }
}