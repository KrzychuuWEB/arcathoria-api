package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.Participant;
import com.arcathoria.monster.dto.MonsterDTO;

final class CombatMapper {

    CombatMapper() {
    }

    static CombatResultDTO fromCombatToCombatResultDTO(final Combat combat) {
        return new CombatResultDTO(
                combat.getSnapshot().combatId().value()
        );
    }

    static CombatResultDTO fromCombatStateToCombatResultDTO(final CombatState state) {
        return new CombatResultDTO(
                state.combatId().value()
        );
    }

    static Participant fromCharacterDTOToParticipant(final CharacterDTO dto) {
        return new Participant(
                dto.id(),
                new Health(dto.health(), dto.health()),
                new Attributes(new Intelligence(dto.intelligence()))
        );
    }

    static Participant fromMonsterDTOToParticipant(final MonsterDTO dto) {
        return new Participant(
                null,
                new Health(dto.maxHealth(), dto.maxHealth()),
                new Attributes(new Intelligence(dto.intelligence()))
        );
    }
}
