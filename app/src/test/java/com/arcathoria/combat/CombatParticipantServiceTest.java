package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

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
        CharacterDTO characterDTO = mapToCharacterDTO(Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build()));

        when(characterClient.getSelectedCharacterByAccountId(any(UUID.class))).thenReturn(characterDTO);

        Participant result = combatParticipantService.getCharacterByAccountId(new AccountId(characterDTO.id()));

        assertThat(result.getId().value()).isEqualTo(characterDTO.id());
        assertThat(result.getHealth().getCurrent()).isEqualTo(characterDTO.health());
        assertThat(result.getIntelligenceLevel()).isEqualTo(characterDTO.intelligence());
    }

    private CharacterDTO mapToCharacterDTO(final Participant participant) {
        return new CharacterDTO(
                participant.getId().value(),
                participant.getId().value().toString(),
                participant.getHealth().getMax(),
                participant.getIntelligenceLevel()
        );
    }
}