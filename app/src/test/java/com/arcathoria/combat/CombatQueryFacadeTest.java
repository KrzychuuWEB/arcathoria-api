package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CombatQueryFacadeTest {

    @Mock
    private GetActiveCombatByParticipantId getActiveCombatByParticipantId;

    @Mock
    private CombatParticipantService participantService;

    @InjectMocks
    private CombatQueryFacade combatQueryFacade;

    @Test
    void should_return_and_map_combat_by_participant_id() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).build());

        when(participantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(participant);
        when(getActiveCombatByParticipantId.getActiveCombat(participant.getId())).thenReturn(combat);

        UUID result = combatQueryFacade.getActiveCombatForSelectedCharacterByAccountId(participant.getId().value());

        assertThat(result).isEqualTo(combat.getSnapshot().combatId().value());

        verify(getActiveCombatByParticipantId).getActiveCombat(participant.getId());
        verify(participantService).getCharacterByAccountId(any(AccountId.class));
    }
}