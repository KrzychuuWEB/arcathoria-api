package com.arcathoria.monster;

import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.vo.MonsterId;

import java.util.UUID;

public class MonsterQueryFacade {

    private final GetMonsterByIdUseCase getMonsterByIdUseCase;

    MonsterQueryFacade(final GetMonsterByIdUseCase getMonsterByIdUseCase) {
        this.getMonsterByIdUseCase = getMonsterByIdUseCase;
    }

    public MonsterDTO getMonsterById(final UUID id) {
        return MonsterMapper.toMonsterDTO(getMonsterByIdUseCase.execute(new MonsterId(id)));
    }
}
