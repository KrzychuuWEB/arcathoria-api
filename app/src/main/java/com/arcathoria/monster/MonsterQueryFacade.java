package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.vo.MonsterId;

public class MonsterQueryFacade {

    private final GetMonsterByIdUseCase getMonsterByIdUseCase;

    MonsterQueryFacade(final GetMonsterByIdUseCase getMonsterByIdUseCase) {
        this.getMonsterByIdUseCase = getMonsterByIdUseCase;
    }

    MonsterDTO getMonsterById(final String id) {
        return MonsterMapper.toMonsterDTO(getMonsterByIdUseCase.execute(new MonsterId(id)));
    }
}
