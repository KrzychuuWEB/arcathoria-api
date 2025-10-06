package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.monster.MonsterQueryFacade;
import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterNotFoundException;
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
@ContextConfiguration(classes = {MonsterClientAdapter.class})
class MonsterClientAdapterTest {

    @MockitoBean
    private MonsterQueryFacade monsterQueryFacade;

    @Autowired
    private MonsterClientAdapter monsterClientAdapter;

    private final MonsterId monsterId = new MonsterId(UUID.randomUUID());

    @Test
    void should_return_a_monster_by_id() {
        MonsterDTO monsterDTO = new MonsterDTO(monsterId.value(), "test_name", 100, 100, 1);
        when(monsterQueryFacade.getMonsterById(monsterId.value())).thenReturn(monsterDTO);

        ParticipantView result = monsterClientAdapter.getMonsterById(monsterId);

        assertThat(result.id()).isEqualTo(monsterDTO.id());
        assertThat(result.name()).isEqualTo(monsterDTO.name());
        assertThat(result.health()).isEqualTo(monsterDTO.maxHealth());
        assertThat(result.intelligence()).isEqualTo(monsterDTO.intelligence());

        verify(monsterQueryFacade).getMonsterById(monsterId.value());
    }

    @Test
    void should_return_CombatParticipantNotFound_when_character_not_selected_for_get_selected_character() {
        when(monsterQueryFacade.getMonsterById(monsterId.value()))
                .thenThrow(new MonsterNotFoundException(new com.arcathoria.monster.vo.MonsterId(monsterId.value())));

        assertThatThrownBy(() -> monsterClientAdapter.getMonsterById(monsterId))
                .isInstanceOf(CombatParticipantNotAvailableException.class)
                .satisfies(e -> {
                    CombatParticipantNotAvailableException ex = (CombatParticipantNotAvailableException) e;
                    assertThat(ex.getUpstreamInfo()).isPresent();
                    assertThat(ex.getUpstreamInfo().get().type()).isEqualTo("monster");
                    assertThat(ex.getUpstreamInfo().get().code()).isEqualTo("ERR_MONSTER_NOT_FOUND");
                    assertThat(ex.getContext()).containsEntry("participantId", monsterId.value());
                });

        verify(monsterQueryFacade).getMonsterById(monsterId.value());
    }

    @Test
    void should_return_ServiceUnavailable_when_monster_query_facade_throws_exception_for_get_monster_by_id() {
        when(monsterQueryFacade.getMonsterById(monsterId.value()))
                .thenThrow(new RuntimeException("test exception"));

        assertThatThrownBy(() -> monsterClientAdapter.getMonsterById(monsterId))
                .isInstanceOf(ExternalServiceUnavailableException.class)
                .satisfies(e -> {
                    ExternalServiceUnavailableException ex = (ExternalServiceUnavailableException) e;
                    assertThat(ex.getContext()).containsEntry("service", "monster");
                });

        verify(monsterQueryFacade).getMonsterById(monsterId.value());
    }
}