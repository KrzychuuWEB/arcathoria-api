package com.arcathoria.combat;

import com.arcathoria.combat.exception.CombatAlreadyFinishedException;
import com.arcathoria.combat.exception.ParticipantNotHasActiveCombatsException;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetActiveCombatByParticipantIdTest {

    @Mock
    private CombatSessionStore combatSessionStore;

    @InjectMocks
    private GetActiveCombatByParticipantId getActiveCombatByParticipantId;

    @Test
    void should_return_combat_when_participant_has_active_combat() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).build());

        when(combatSessionStore.getActiveCombatByParticipantId(participant.getId())).thenReturn(Optional.of(combat.getSnapshot()));

        Combat result = getActiveCombatByParticipantId.getActiveCombat(participant.getId());

        assertThat(result.getSnapshot().combatId()).isEqualTo(combat.getSnapshot().combatId());
        assertThat(result.getSnapshot().attacker().participantId()).isEqualTo(combat.getSnapshot().attacker().participantId());
        assertThat(result.getSnapshot().combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);

        verify(combatSessionStore).getActiveCombatByParticipantId(participant.getId());
    }

    @Test
    void should_return_ParticipantNotHasActiveCombatsException_when_participant_not_has_active_combat() {
        when(combatSessionStore.getActiveCombatByParticipantId(any(ParticipantId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getActiveCombatByParticipantId.getActiveCombat(new ParticipantId(UUID.randomUUID())))
                .isInstanceOf(ParticipantNotHasActiveCombatsException.class)
                .hasMessageContaining("not has active combats");

        verify(combatSessionStore).getActiveCombatByParticipantId(any(ParticipantId.class));
    }

    @Test
    void should_return_CombatAlreadyFinishedException_when_combat_have_combat_status_other_than_in_progress() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).withCombatStatus(CombatStatus.FINISHED).build());

        when(combatSessionStore.getActiveCombatByParticipantId(any(ParticipantId.class))).thenReturn(Optional.of(combat.getSnapshot()));

        assertThatThrownBy(() -> getActiveCombatByParticipantId.getActiveCombat(participant.getId()))
                .isInstanceOf(CombatAlreadyFinishedException.class)
                .hasMessageContaining("is already finished, this action cannot be performed");
        assertThat(combat.getSnapshot().combatStatus()).isEqualTo(CombatStatus.FINISHED);

        verify(combatSessionStore).getActiveCombatByParticipantId(participant.getId());
    }
}