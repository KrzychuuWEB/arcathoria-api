package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.monster.dto.MonsterDTO;

class ParticipantMapperHelper {

    static CharacterDTO fromParticipantToCharacterDTO(final Participant participant) {
        return new CharacterDTO(
                participant.getId().value(),
                participant.getId().value().toString(),
                participant.getHealth().getMax(),
                participant.getIntelligenceLevel()
        );
    }
    
    static MonsterDTO fromParticipantToMonsterDTO(final Participant participant) {
        return new MonsterDTO(
                participant.getId().value(),
                participant.getId().toString(),
                participant.getHealth().getCurrent(),
                participant.getHealth().getMax(),
                participant.getIntelligenceLevel()
        );
    }
}
