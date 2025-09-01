package com.arcathoria.combat;

import com.arcathoria.monster.MonsterQueryFacade;
import com.arcathoria.monster.dto.MonsterDTO;

class MonsterClient {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterClient(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    MonsterDTO getMonster(final String monsterId) {
        return monsterQueryFacade.getMonsterById(monsterId);
    }
}
