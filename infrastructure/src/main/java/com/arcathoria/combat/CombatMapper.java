package com.arcathoria.combat;

import com.arcathoria.character.vo.Gauge;
import com.arcathoria.character.vo.Health;
import com.arcathoria.character.vo.Intelligence;
import com.arcathoria.character.vo.Level;
import com.arcathoria.combat.vo.Attributes;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.ParticipantId;

final class CombatMapper {

    CombatMapper() {
    }

    static Combat toDomain(final CombatEntity entity) {
        ParticipantEntity attacker = getParticipantEntityBySide(entity, CombatSide.ATTACKER);
        ParticipantEntity defender = getParticipantEntityBySide(entity, CombatSide.DEFENDER);

        return Combat.restore(
                new CombatSnapshot(
                        new CombatId(entity.getId()),
                        new ParticipantSnapshot(
                                new ParticipantId(attacker.getCharacterId()),
                                new Health(new Gauge(attacker.getCurrentHealth(), attacker.getMaxHealth())),
                                new Attributes(
                                        new Intelligence(new Level(attacker.getIntelligence()))
                                )
                        ),
                        new ParticipantSnapshot(
                                new ParticipantId(defender.getCharacterId()),
                                new Health(new Gauge(defender.getCurrentHealth(), defender.getMaxHealth())),
                                new Attributes(
                                        new Intelligence(new Level(defender.getIntelligence()))
                                )
                        ),
                        new CombatTurn(entity.getSide()),
                        entity.getType(),
                        entity.getStatus()
                )
        );
    }

    static CombatEntity toEntityForInsert(final Combat domain) {
        CombatSnapshot snapshot = domain.getSnapshot();
        ParticipantSnapshot attacker = snapshot.attacker();
        ParticipantSnapshot defender = snapshot.defender();

        CombatEntity combatEntity = new CombatEntity(
                snapshot.combatId().value(),
                snapshot.combatType(),
                snapshot.combatTurn().currentSide(),
                snapshot.combatStatus()
        );

        combatEntity.addParticipant(mapToParticipantEntityWithSide(attacker, combatEntity, CombatSide.ATTACKER));
        combatEntity.addParticipant(mapToParticipantEntityWithSide(defender, combatEntity, CombatSide.DEFENDER));

        return combatEntity;
    }

    private static ParticipantEntity mapToParticipantEntityWithSide(final ParticipantSnapshot snapshot, final CombatEntity combatEntity, final CombatSide side) {
        return new ParticipantEntity(
                null,
                combatEntity,
                snapshot.participantId().value(),
                side,
                snapshot.health().getCurrent(),
                snapshot.health().getMax(),
                snapshot.attributes().intelligence().level().value()
        );
    }

    private static ParticipantEntity getParticipantEntityBySide(final CombatEntity entity, final CombatSide side) {
        return entity.getParticipants().stream()
                .filter(p -> side.equals(p.getSide()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Participant not found in combat:  " + entity.getId()));
    }
}
