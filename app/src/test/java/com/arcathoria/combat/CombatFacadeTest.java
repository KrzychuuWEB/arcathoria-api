package com.arcathoria.combat;

import com.arcathoria.combat.command.ExecuteActionCommand;
import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.ExecuteActionDTO;
import com.arcathoria.combat.dto.InitPveDTO;
import com.arcathoria.combat.vo.CombatId;
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
class CombatFacadeTest {

    @Mock
    private InitialPVECombatUseCase initialPVECombatUseCase;

    @Mock
    private ExecuteCombatActionUseCase executeCombatActionUseCase;

    @InjectMocks
    private CombatFacade combatFacade;

    @Test
    void should_return_combatResultDTO_after_initial_combat() {
        UUID combatId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().withCombatId(new CombatId(combatId)).build();

        when(initialPVECombatUseCase.init(any(InitPVECombatCommand.class))).thenReturn(snapshot);

        InitPveDTO initPveDTO = new InitPveDTO(UUID.randomUUID());
        CombatResultDTO result = combatFacade.initPVECombat(accountId, initPveDTO);

        assertThat(result.combatId()).isEqualTo(combatId);
    }

    @Test
    void should_return_combatResultDTO_when_perform_action_in_combat() {
        UUID combatId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().withCombatId(new CombatId(combatId)).build();

        when(executeCombatActionUseCase.performAction(any(ExecuteActionCommand.class))).thenReturn(snapshot);

        ExecuteActionDTO command = new ExecuteActionDTO(combatId, "melee");
        CombatResultDTO result = combatFacade.performActionInCombat(accountId, command);

        assertThat(result.combatId()).isEqualTo(combatId);
    }
}