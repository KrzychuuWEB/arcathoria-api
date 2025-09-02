package com.arcathoria.combat;

import com.arcathoria.combat.command.StartPVECombatCommand;
import com.arcathoria.combat.dto.CombatResultDTO;

import static com.arcathoria.combat.CombatMapper.fromCombatStateToCombatResultDTO;

public class CombatFacade {

    private final StartPVECombatUseCase startPVECombatUseCase;

    CombatFacade(final StartPVECombatUseCase startPVECombatUseCase) {
        this.startPVECombatUseCase = startPVECombatUseCase;
    }

    CombatResultDTO startPVE(final StartPVECombatCommand command) {
        return fromCombatStateToCombatResultDTO(startPVECombatUseCase.execute(command));
    }
}
