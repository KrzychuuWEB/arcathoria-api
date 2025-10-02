package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableDomainException;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;
import com.arcathoria.monster.MonsterQueryFacade;
import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import org.springframework.stereotype.Component;

@Component
class MonsterClientAdapter implements MonsterClient {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterClientAdapter(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    @Override
    public ParticipantView getMonsterById(final MonsterId monsterId) {
        try {
            return mapToParticipantView(monsterQueryFacade.getMonsterById(monsterId.value()));
        } catch (MonsterNotFoundException e) {
            throw new CombatParticipantNotAvailableDomainException(new ParticipantId(e.getMonsterId().value()),
                    new UpstreamInfo(e.getDomain(), e.getErrorCode().getCodeName()));
        }
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
