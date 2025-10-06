package com.arcathoria.combat;

import com.arcathoria.character.CharacterQueryFacade;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.exception.CharacterNotSelectedException;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.AccountId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@ContextConfiguration(classes = {CharacterClientAdapter.class})
class CharacterClientAdapterTest {

    @MockitoBean
    private CharacterQueryFacade characterQueryFacade;

    @Autowired
    private CharacterClientAdapter characterClientAdapter;

    private final AccountId accountId = new AccountId(UUID.randomUUID());

    @Test
    void should_get_selected_character_by_account_id() {
        CharacterDTO characterDTO = new CharacterDTO(
                UUID.randomUUID(),
                "test_char_nem",
                100,
                1
        );

        when(characterQueryFacade.getSelectedCharacter(accountId.value())).thenReturn(characterDTO);

        ParticipantView result = characterClientAdapter.getSelectedCharacterByAccountId(accountId);

        assertThat(result.id()).isEqualTo(characterDTO.id());
        assertThat(result.name()).isEqualTo(characterDTO.characterName());
        assertThat(result.health()).isEqualTo(characterDTO.health());
        assertThat(result.intelligence()).isEqualTo(characterDTO.intelligence());

        verify(characterQueryFacade).getSelectedCharacter(accountId.value());
    }

    @Test
    void should_return_CombatParticipantNotFound_when_character_not_found_for_get_selected_character() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());

        when(characterQueryFacade.getSelectedCharacter(accountId.value())).thenThrow(new CharacterNotFoundException(characterId));

        assertThatThrownBy(() -> characterClientAdapter.getSelectedCharacterByAccountId(accountId))
                .isInstanceOf(CombatParticipantNotAvailableException.class)
                .satisfies(e -> {
                    CombatParticipantNotAvailableException ex = (CombatParticipantNotAvailableException) e;
                    assertThat(ex.getUpstreamInfo()).isPresent();
                    assertThat(ex.getUpstreamInfo().get().type()).isEqualTo("character");
                    assertThat(ex.getUpstreamInfo().get().code()).isEqualTo("ERR_CHARACTER_NOT_FOUND");
                    assertThat(ex.getContext()).containsEntry("participantId", characterId.value());
                });
        verify(characterQueryFacade).getSelectedCharacter(accountId.value());
    }

    @Test
    void should_return_CombatParticipantNotFound_when_character_not_selected_for_get_selected_character() {
        when(characterQueryFacade.getSelectedCharacter(accountId.value()))
                .thenThrow(new CharacterNotSelectedException(new com.arcathoria.character.vo.AccountId(accountId.value())));

        assertThatThrownBy(() -> characterClientAdapter.getSelectedCharacterByAccountId(accountId))
                .isInstanceOf(CombatParticipantNotAvailableException.class)
                .satisfies(e -> {
                    CombatParticipantNotAvailableException ex = (CombatParticipantNotAvailableException) e;
                    assertThat(ex.getUpstreamInfo()).isPresent();
                    assertThat(ex.getUpstreamInfo().get().type()).isEqualTo("character");
                    assertThat(ex.getUpstreamInfo().get().code()).isEqualTo("ERR_CHARACTER_NOT_SELECTED");
                    assertThat(ex.getContext()).containsEntry("participantId", accountId.value());
                });

        verify(characterQueryFacade).getSelectedCharacter(accountId.value());
    }

    @Test
    void should_return_ServiceUnavailable_when_character_query_facade_throws_exception() {
        when(characterQueryFacade.getSelectedCharacter(accountId.value()))
                .thenThrow(new RuntimeException("test exception"));

        assertThatThrownBy(() -> characterClientAdapter.getSelectedCharacterByAccountId(accountId))
                .isInstanceOf(ExternalServiceUnavailableException.class)
                .satisfies(e -> {
                    ExternalServiceUnavailableException ex = (ExternalServiceUnavailableException) e;
                    assertThat(ex.getContext()).containsEntry("service", "character");
                });
    }
}