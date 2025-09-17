package com.arcathoria.combat;

import com.arcathoria.combat.command.ExecuteActionCommand;
import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.dto.CombatResultDTO;

import static com.arcathoria.combat.CombatMapper.fromCombatStateToCombatResultDTO;

public class CombatFacade {

    private final InitialPVECombatUseCase initialPVECombatUseCase;
    private final ExecuteCombatActionUseCase executeCombatActionUseCase;

    CombatFacade(final InitialPVECombatUseCase initialPVECombatUseCase,
                 final ExecuteCombatActionUseCase executeCombatActionUseCase) {
        this.initialPVECombatUseCase = initialPVECombatUseCase;
        this.executeCombatActionUseCase = executeCombatActionUseCase;
    }

    CombatResultDTO initPVECombat(final InitPVECombatCommand command) {
        return fromCombatStateToCombatResultDTO(initialPVECombatUseCase.init(command));
    }

    CombatResultDTO performActionInCombat(final ExecuteActionCommand command) {
        return fromCombatStateToCombatResultDTO(executeCombatActionUseCase.performAction(command));
    }
}
