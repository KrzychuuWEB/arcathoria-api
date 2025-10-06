package com.arcathoria.combat;

import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.testContainers.WithPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@WithPostgres
@Import({CombatRepositoryAdapter.class})
class CombatRepositoryAdapterTest {

    @Autowired
    private CombatRepositoryAdapter repository;

    @Test
    void should_return_combat_after_save_in_repository() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).build());

        Combat result = repository.save(combat);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().combatId()).isEqualTo(combat.getSnapshot().combatId());
        assertThat(result.getSnapshot().combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);
        assertThat(result.getSnapshot().attacker().participantId()).isEqualTo(participant.getId());
    }
}