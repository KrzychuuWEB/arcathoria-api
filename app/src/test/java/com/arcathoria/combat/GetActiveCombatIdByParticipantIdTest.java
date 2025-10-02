package com.arcathoria.combat;

import com.arcathoria.combat.exception.ParticipantNotHasActiveCombatsDomainException;
import com.arcathoria.combat.vo.CombatId;
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
class GetActiveCombatIdByParticipantIdTest {

    @Mock
    private CombatSessionStore combatSessionStore;

    @InjectMocks
    private GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId;

    @Test
    void should_return_combat_when_participant_has_active_combat() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).build());

        when(combatSessionStore.getActiveCombatIdByParticipantId(participant.getId())).thenReturn(Optional.of((combat.getSnapshot().combatId())));

        CombatId result = getActiveCombatIdByParticipantId.getActiveCombat(participant.getId());

        assertThat(result).isEqualTo(combat.getSnapshot().combatId());

        verify(combatSessionStore).getActiveCombatIdByParticipantId(participant.getId());
    }

    @Test
    void should_return_ParticipantNotHasActiveCombatsException_when_participant_not_has_active_combat() {
        when(combatSessionStore.getActiveCombatIdByParticipantId(any(ParticipantId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getActiveCombatIdByParticipantId.getActiveCombat(new ParticipantId(UUID.randomUUID())))
                .isInstanceOf(ParticipantNotHasActiveCombatsDomainException.class)
                .hasMessageContaining("not has active combats");

        verify(combatSessionStore).getActiveCombatIdByParticipantId(any(ParticipantId.class));
    }
}