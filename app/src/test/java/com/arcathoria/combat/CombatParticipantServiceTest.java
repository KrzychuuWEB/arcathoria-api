package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.vo.AccountId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CombatParticipantServiceTest {

    @Mock
    private CharacterClient characterClient;

    @InjectMocks
    CombatParticipantService combatParticipantService;

    @Test
    void should_return_character_by_account_id() {
        ParticipantView characterDTO = mapToParticipantView(Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build()));

        when(characterClient.getSelectedCharacterByAccountId(any(AccountId.class))).thenReturn(characterDTO);

        Participant result = combatParticipantService.getCharacterByAccountId(new AccountId(characterDTO.id()));

        assertThat(result.getId().value()).isEqualTo(characterDTO.id());
        assertThat(result.getHealth().getCurrent()).isEqualTo(characterDTO.health());
        assertThat(result.getIntelligenceLevel()).isEqualTo(characterDTO.intelligence());
    }

    private ParticipantView mapToParticipantView(final Participant participant) {
        return new ParticipantView(
                participant.getId().value(),
                participant.getId().value().toString(),
                participant.getHealth().getMax(),
                participant.getIntelligenceLevel(),
                participant.getSnapshot().participantType()
        );
    }
}