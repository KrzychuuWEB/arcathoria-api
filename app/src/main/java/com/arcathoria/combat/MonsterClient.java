package com.arcathoria.combat;

import com.arcathoria.monster.dto.MonsterDTO;

interface MonsterClient {

    MonsterDTO getMonsterById(final String monsterId);
}
