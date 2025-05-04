package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;

class MonsterMapper {

    static MonsterDTO toMonsterDTO(Monster monster) {
        return new MonsterDTO(
                monster.getSnapshot().monsterId().value(),
                monster.getSnapshot().monsterName().value(),
                monster.getSnapshot().health().getCurrent(),
                monster.getSnapshot().health().getMax()
        );
    }
}
