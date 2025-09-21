package com.arcathoria.combat;

import com.arcathoria.monster.dto.MonsterDTO;

import java.util.Optional;
import java.util.UUID;

interface MonsterClient {

    Optional<MonsterDTO> getMonsterById(final UUID monsterId);
}
