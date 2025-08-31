package com.arcathoria.combat;

import com.arcathoria.combat.command.StartPVECombatCommand;

class StartPVECombatUseCase {

    private final CombatEngine combatEngine;
    private final CombatRepository combatRepository;
    private final CombatStateRepository combatStateRepository;

    StartPVECombatUseCase(
            final CombatEngine combatEngine,
            final CombatRepository combatRepository,
            final CombatStateRepository combatStateRepository
    ) {
        this.combatEngine = combatEngine;
        this.combatRepository = combatRepository;
        this.combatStateRepository = combatStateRepository;
    }

    Combat execute(final StartPVECombatCommand command) {
        return combatEngine.startCombat(command.attacker(), command.defender(), CombatType.PVE);
    }
}
