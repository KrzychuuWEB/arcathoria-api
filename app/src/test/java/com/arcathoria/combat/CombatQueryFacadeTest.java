package com.arcathoria.combat;

import com.arcathoria.combat.dto.CombatIdDTO;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.CombatId;
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
    private GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId;

    @Mock
    private CombatParticipantService participantService;

    @Mock
    private GetCombatFromStoreByIdAndParticipantId getCombatFromStoreByIdAndParticipantId;

    @InjectMocks
    private CombatQueryFacade combatQueryFacade;

    @Test
    void should_return_and_map_for_get_combat_for_selected_character() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).build());

        when(participantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(participant);
        when(getActiveCombatIdByParticipantId.getActiveCombat(participant.getId())).thenReturn(combat.getSnapshot().combatId());

        CombatIdDTO result = combatQueryFacade.getActiveCombatForSelectedCharacterByAccountId(participant.getId().value());

        assertThat(result.combatId()).isEqualTo(combat.getSnapshot().combatId().value());

        verify(getActiveCombatIdByParticipantId).getActiveCombat(participant.getId());
        verify(participantService).getCharacterByAccountId(any(AccountId.class));
    }

    @Test
    void should_return_map_combat_for_get_combat_by_combat_id_and_participant_id() {
        Participant participant = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().withAttacker(participant.getSnapshot()).build();

        when(participantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(participant);
        when(getCombatFromStoreByIdAndParticipantId.getByCombatIdAndParticipantId(any(CombatId.class), any(ParticipantId.class))).thenReturn(snapshot);

        CombatResultDTO result = combatQueryFacade.getCombatByIdAndParticipantId(snapshot.combatId().value(), snapshot.attacker().participantId().value());

        assertThat(result.combatId()).isEqualTo(snapshot.combatId().value());
        assertThat(result.attacker().id()).isEqualTo(snapshot.attacker().participantId().value());
    }
}