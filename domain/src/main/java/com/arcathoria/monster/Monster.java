package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

class Monster {

    static Monster restore(final MonsterSnapshot snapshot) {
        return new Monster(
                snapshot.monsterId(),
                snapshot.monsterName(),
                snapshot.health()
        );
    }

    private final MonsterId monsterId;
    private final MonsterName monsterName;
    private final Health health;

    private Monster(final MonsterId monsterId, final MonsterName monsterName, final Health health) {
        this.monsterId = monsterId;
        this.monsterName = monsterName;
        this.health = health;
    }

    MonsterSnapshot getSnapshot() {
        return new MonsterSnapshot(
                monsterId,
                monsterName,
                health
        );
    }
}
