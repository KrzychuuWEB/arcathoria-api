package com.arcathoria.monster;

import com.arcathoria.monster.exception.MonsterNotFoundException;
import com.arcathoria.monster.vo.MonsterId;

class GetMonsterByIdUseCase {

    private final MonsterQueryRepository monsterQueryRepository;

    GetMonsterByIdUseCase(final MonsterQueryRepository monsterQueryRepository) {
        this.monsterQueryRepository = monsterQueryRepository;
    }

    Monster execute(final MonsterId monsterId) {
        return monsterQueryRepository.getById(monsterId)
                .orElseThrow(() -> new MonsterNotFoundException(monsterId.value()));
    }
}
