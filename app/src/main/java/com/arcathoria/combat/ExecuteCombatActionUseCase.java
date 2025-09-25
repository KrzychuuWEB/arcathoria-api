package com.arcathoria.combat;

import com.arcathoria.combat.command.ExecuteActionCommand;

class ExecuteCombatActionUseCase {

    private final CombatEngine combatEngine;
    private final GetCombatSnapshotFromStore getCombatSnapshotFromStore;
    private final CombatParticipantService combatParticipantService;
    private final CombatSessionStore combatSessionStore;
    private final CombatRepository combatRepository;
    private final CombatActionRegistry combatActionRegistry;

    ExecuteCombatActionUseCase(
            final CombatEngine combatEngine,
            final GetCombatSnapshotFromStore getCombatSnapshotFromStore,
            final CombatParticipantService combatParticipantService,
            final CombatSessionStore combatSessionStore,
            final CombatRepository combatRepository,
            final CombatActionRegistry combatActionRegistry
    ) {
        this.combatEngine = combatEngine;
        this.getCombatSnapshotFromStore = getCombatSnapshotFromStore;
        this.combatParticipantService = combatParticipantService;
        this.combatSessionStore = combatSessionStore;
        this.combatRepository = combatRepository;
        this.combatActionRegistry = combatActionRegistry;
    }

    CombatSnapshot performAction(final ExecuteActionCommand command) {
        Combat combat = Combat.restore(
                getCombatSnapshotFromStore.getSnapshotById(command.combatId())
        );

        Participant participant = combatParticipantService.getCharacterByAccountId(command.accountId());
        Participant participantFromStore = combat.getParticipant(participant.getId());

        Combat result = combatEngine.handleAction(combat, combatActionRegistry.get(command.actionType()), participantFromStore);

        if (result.getCombatStatus() == CombatStatus.FINISHED) {
            combatRepository.save(result).getSnapshot();
        }

        return combatSessionStore.save(result.getSnapshot());
    }
}