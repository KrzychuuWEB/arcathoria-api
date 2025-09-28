package com.arcathoria.combat;

import com.arcathoria.combat.command.ExecuteActionCommand;
import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.ExecuteActionDTO;
import com.arcathoria.combat.dto.InitPveDTO;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.MonsterId;

import java.util.UUID;

import static com.arcathoria.combat.CombatDTOMapper.fromCombatStateToCombatResultDTO;

public class CombatFacade {

    private final InitialPVECombatUseCase initialPVECombatUseCase;
    private final ExecuteCombatActionUseCase executeCombatActionUseCase;

    CombatFacade(final InitialPVECombatUseCase initialPVECombatUseCase,
                 final ExecuteCombatActionUseCase executeCombatActionUseCase) {
        this.initialPVECombatUseCase = initialPVECombatUseCase;
        this.executeCombatActionUseCase = executeCombatActionUseCase;
    }

    CombatResultDTO initPVECombat(final UUID accountId, final InitPveDTO dto) {
        InitPVECombatCommand command = new InitPVECombatCommand(
                new AccountId(accountId),
                new MonsterId(dto.monsterId())
        );
        return fromCombatStateToCombatResultDTO(initialPVECombatUseCase.init(command));
    }

    CombatResultDTO performActionInCombat(final UUID accountId, final ExecuteActionDTO dto) {
        ExecuteActionCommand command = new ExecuteActionCommand(
                new CombatId(dto.combatId()),
                new AccountId(accountId),
                ActionType.valueOf(dto.actionType().toUpperCase())
        );

        return fromCombatStateToCombatResultDTO(executeCombatActionUseCase.performAction(command));
    }
}
