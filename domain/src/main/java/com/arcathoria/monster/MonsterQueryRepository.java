package com.arcathoria.monster;

import com.arcathoria.monster.vo.MonsterId;

interface MonsterQueryRepository {

    Monster getById(final MonsterId monsterId);
}
