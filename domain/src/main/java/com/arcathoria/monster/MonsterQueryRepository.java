package com.arcathoria.monster;

import com.arcathoria.monster.vo.MonsterId;

import java.util.Optional;

interface MonsterQueryRepository {

    Optional<Monster> getById(final MonsterId monsterId);
}
