package com.arcathoria.combat;

import com.arcathoria.Gauge;
import com.arcathoria.Level;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.ParticipantDTO;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.Health;
import com.arcathoria.combat.vo.Intelligence;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.monster.dto.MonsterDTO;

final class CombatDTOMapper {

    CombatDTOMapper() {
    }

    static CombatResultDTO fromCombatStateToCombatResultDTO(final CombatSnapshot snapshot) {
        return new CombatResultDTO(
                snapshot.combatId().value(),
                fromParticipantSnapshotToParticipantDTO(snapshot.attacker()),
                fromParticipantSnapshotToParticipantDTO(snapshot.defender()),
                snapshot.combatStatus(),
                snapshot.combatTurn().currentSide()
        );
    }

    static Participant fromCharacterDTOToParticipant(final CharacterDTO dto) {
        return Participant.restore(new ParticipantSnapshot(
                new ParticipantId(dto.id()),
                new Health(new Gauge(dto.health(), dto.health())),
                new Attributes(new Intelligence(new Level(dto.intelligence()))),
                ParticipantType.PLAYER
        ));
    }


    static Participant fromMonsterDTOToParticipant(final MonsterDTO dto) {
        return Participant.restore(new ParticipantSnapshot(
                new ParticipantId(dto.id()),
                new Health(new Gauge(dto.maxHealth(), dto.maxHealth())),
                new Attributes(new Intelligence(new Level(dto.intelligence()))),
                ParticipantType.MONSTER
        ));
    }

    static ParticipantDTO fromParticipantSnapshotToParticipantDTO(final ParticipantSnapshot snapshot) {
        return new ParticipantDTO(
                snapshot.participantId().value(),
                snapshot.health().getCurrent(),
                snapshot.health().getMax()
        );
    }
}
