package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.vo.Gauge;
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

    static CombatResultDTO fromCombatStateToCombatResultDTO(final CombatSnapshot snapshot) {
        return new CombatResultDTO(
                snapshot.combatId().value()
        );
    }

    static Participant fromCharacterDTOToParticipant(final CharacterDTO dto) {
        return new Participant(
                new ParticipantId(dto.id()),
                new Health(new Gauge(dto.health(), dto.health())),
                new Attributes(new Intelligence(new Level(dto.intelligence())))
        );
    }

    static Participant fromMonsterDTOToParticipant(final MonsterDTO dto) {
        return new Participant(
                new ParticipantId(dto.id()),
                new Health(new Gauge(dto.maxHealth(), dto.maxHealth())),
                new Attributes(new Intelligence(new Level(dto.intelligence())))
        );
    }
}
