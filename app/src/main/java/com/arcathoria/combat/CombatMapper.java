package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.character.vo.Level;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.monster.dto.MonsterDTO;

final class CombatMapper {

    CombatMapper() {
    }

    static CombatResultDTO fromCombatStateToCombatResultDTO(final CombatState state) {
        return new CombatResultDTO(
                state.combatId().value()
        );
    }

    static Participant fromCharacterDTOToParticipant(final CharacterDTO dto) {
        return new Participant(
                new ParticipantId(dto.id()),
                new Health(dto.health(), dto.health()),
                new Attributes(new Intelligence(new Level(dto.intelligence())))
        );
    }

    static Participant fromMonsterDTOToParticipant(final MonsterDTO dto) {
        return new Participant(
                new ParticipantId(dto.id()),
                new Health(dto.maxHealth(), dto.maxHealth()),
                new Attributes(new Intelligence(new Level(dto.intelligence())))
        );
    }
}
