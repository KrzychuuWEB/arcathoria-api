package com.arcathoria.combat;

import com.arcathoria.monster.dto.MonsterDTO;

import java.util.UUID;

interface MonsterClient {

    MonsterDTO getMonsterById(final UUID monsterId);
}
