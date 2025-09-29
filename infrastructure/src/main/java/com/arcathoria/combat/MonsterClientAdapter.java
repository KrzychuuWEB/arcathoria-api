package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.monster.MonsterQueryFacade;
import com.arcathoria.monster.dto.MonsterDTO;
import org.springframework.stereotype.Component;

@Component
class MonsterClientAdapter implements MonsterClient {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterClientAdapter(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    @Override
    public ParticipantView getMonsterById(final MonsterId monsterId) {
        return mapToParticipantView(monsterQueryFacade.getMonsterById(monsterId.value()));
    }

    private ParticipantView mapToParticipantView(final MonsterDTO monsterDTO) {
        return new ParticipantView(
                monsterDTO.id(),
                monsterDTO.name(),
                monsterDTO.maxHealth(),
                monsterDTO.intelligence(),
                ParticipantType.MONSTER
        );
    }
}
