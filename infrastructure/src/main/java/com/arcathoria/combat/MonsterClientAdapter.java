package com.arcathoria.combat;

import com.arcathoria.ApiException;
import com.arcathoria.monster.MonsterQueryFacade;
import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class MonsterClientAdapter implements MonsterClient {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterClientAdapter(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    @Override
    public Optional<MonsterDTO> getMonsterById(final UUID monsterId) throws MonsterNotFoundException {
        try {
            return Optional.ofNullable(monsterQueryFacade.getMonsterById(monsterId));
        } catch (ApiException e) {
            return Optional.empty();
        }
    }
}
