package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

class MonsterSnapshot {

    private final MonsterId monsterId;
    private final MonsterName monsterName;
    private final Health health;

    MonsterSnapshot(final MonsterId monsterId, final MonsterName monsterName, final Health health) {
        this.monsterId = monsterId;
        this.monsterName = monsterName;
        this.health = health;
    }

    MonsterId getMonsterId() {
        return monsterId;
    }

    MonsterName getMonsterName() {
        return monsterName;
    }

    Health getHealth() {
        return health;
    }
}
