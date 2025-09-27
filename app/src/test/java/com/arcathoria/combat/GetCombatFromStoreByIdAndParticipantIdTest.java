package com.arcathoria.combat;

import com.arcathoria.combat.exception.ParticipantNotFoundInCombatException;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCombatFromStoreByIdAndParticipantIdTest {

    @Mock
    private GetCombatSnapshotFromStore getCombatSnapshotFromStore;

    @InjectMocks
    private GetCombatFromStoreByIdAndParticipantId getCombatFromStoreByIdAndParticipantId;

    @Test
    void should_return_combat_snapshot_when_combat_id_and_participant_id_is_correct() {
        ParticipantSnapshot participant = ParticipantSnapshotMother.aParticipantBuilder().build();
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().build();

        when(getCombatSnapshotFromStore.getSnapshotById(snapshot.combatId())).thenReturn(snapshot);

        CombatSnapshot result = getCombatFromStoreByIdAndParticipantId.getByCombatIdAndParticipantId(snapshot.combatId(), participant.participantId());

        assertThat(result).isEqualTo(snapshot);
        assertThat(result.combatId()).isEqualTo(snapshot.combatId());

        verify(getCombatSnapshotFromStore).getSnapshotById(snapshot.combatId());
    }

    @Test
    void should_return_ParticipantNotFoundInCombatException_when_participant_has_active_combat() {
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().build();

        ParticipantId otherParticipant = new ParticipantId(UUID.randomUUID());
        when(getCombatSnapshotFromStore.getSnapshotById(snapshot.combatId())).thenReturn(snapshot);

        assertThatThrownBy(() -> getCombatFromStoreByIdAndParticipantId.getByCombatIdAndParticipantId(snapshot.combatId(), otherParticipant))
                .isInstanceOf(ParticipantNotFoundInCombatException.class)
                .hasMessageContaining(otherParticipant.value().toString());

        verify(getCombatSnapshotFromStore).getSnapshotById(snapshot.combatId());
    }
}