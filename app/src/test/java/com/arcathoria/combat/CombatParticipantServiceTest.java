package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        Participant result = combatParticipantService.getCharacterByAccountId(characterDTO.id());

        assertThat(result.getId().value()).isEqualTo(characterDTO.id());
        assertThat(result.getHealth().getCurrent()).isEqualTo(characterDTO.health());
        assertThat(result.getIntelligenceLevel()).isEqualTo(characterDTO.intelligence());
    }

    @Test
    void should_return_CombatParticipantUnavailableException_when_character_not_found() {
        when(characterClient.getSelectedCharacterByAccountId(any(UUID.class))).thenThrow(CharacterNotFoundException.class);

        assertThatThrownBy(() -> combatParticipantService.getCharacterByAccountId(UUID.randomUUID()))
                .isInstanceOf(CombatParticipantUnavailableException.class)
                .message().isEqualTo("Could not retrieve participant for combat.");
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