package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;

class MonsterMapper {

    private MonsterMapper() {
    }

    static MonsterDTO toMonsterDTO(Monster monster) {
        return new MonsterDTO(
                monster.getSnapshot().monsterId().value(),
                monster.getSnapshot().monsterName().value(),
                monster.getSnapshot().health().getCurrent(),
                monster.getSnapshot().health().getMax(),
                monster.getSnapshot().attributes().intelligence().level().value()
        );
    }
}
