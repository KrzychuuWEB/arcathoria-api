package com.arcathoria.monster;

import com.arcathoria.character.vo.Health;

class Monster {

    static Monster restore(final MonsterSnapshot snapshot) {
        return new Monster();
    }

    private final String monsterCode;
    private final String monsterName;
    private final Health health;

    private Monster(final String monsterCode, final String monsterName, final Health health) {
        this.monsterCode = monsterCode;
        this.monsterName = monsterName;
        this.health = health;
    }

    MonsterSnapshot getSnapshot() {
        return new MonsterSnapshot();
    }
}
