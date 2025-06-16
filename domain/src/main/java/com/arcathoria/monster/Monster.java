package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.monster.vo.MonsterId;
import com.arcathoria.monster.vo.MonsterName;

class Monster {

    static Monster restore(final MonsterSnapshot snapshot) {
        return new Monster(
                snapshot.monsterId(),
                snapshot.monsterName(),
                snapshot.health(),
                snapshot.attributes()
        );
    }

    private final MonsterId monsterId;
    private final MonsterName monsterName;
    private final Health health;
    private final Attributes attributes;

    private Monster(final MonsterId monsterId, final MonsterName monsterName, final Health health, final Attributes attributes) {
        this.monsterId = monsterId;
        this.monsterName = monsterName;
        this.health = health;
        this.attributes = attributes;
    }

    MonsterSnapshot getSnapshot() {
        return new MonsterSnapshot(
                monsterId,
                monsterName,
                health,
                attributes
        );
    }
}
