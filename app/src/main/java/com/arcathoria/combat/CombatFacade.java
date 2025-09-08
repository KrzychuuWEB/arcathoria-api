package com.arcathoria.combat;

import com.arcathoria.combat.command.StartPVECombatCommand;
import com.arcathoria.combat.dto.CombatResultDTO;

import static com.arcathoria.combat.CombatMapper.fromCombatStateToCombatResultDTO;

public class CombatFacade {

    private final InitialPVECombatUseCase initialPVECombatUseCase;

    CombatFacade(final InitialPVECombatUseCase initialPVECombatUseCase) {
        this.initialPVECombatUseCase = initialPVECombatUseCase;
    }

    CombatResultDTO initPVECombat(final StartPVECombatCommand command) {
        return fromCombatStateToCombatResultDTO(initialPVECombatUseCase.execute(command));
    }
}
