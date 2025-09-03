package com.arcathoria.combat;

import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterNotFoundException;

import java.util.UUID;

interface MonsterClient {

    MonsterDTO getMonsterById(final UUID monsterId) throws MonsterNotFoundException;
}
