package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

public final class MonsterSnapshotMother {
    public static final MonsterId DEFAULT_MONSTER_ID = new MonsterId(null);
    public static final MonsterName DEFAULT_MONSTER_NAME = new MonsterName("Wolf");
    public static final Health DEFAULT_HEALTH = new Health(100.0, 100.0);

    private MonsterId monsterId = DEFAULT_MONSTER_ID;
    private MonsterName monsterName = DEFAULT_MONSTER_NAME;
    private Health health = DEFAULT_HEALTH;

    private MonsterSnapshotMother() {
    }

    static MonsterSnapshotMother aMonsterSnapshot() {
        return new MonsterSnapshotMother();
    }

    MonsterSnapshotMother withMonsterId(final MonsterId monsterId) {
        this.monsterId = monsterId;
        return this;
    }

    MonsterSnapshotMother withMonsterName(final MonsterName monsterName) {
        this.monsterName = monsterName;
        return this;
    }

    MonsterSnapshotMother withHealth(final Health health) {
        this.health = health;
        return this;
    }

    MonsterSnapshot build() {
        return new MonsterSnapshot(monsterId, monsterName, health);
    }
}
