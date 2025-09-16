package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.vo.CombatId;

class ExecuteCombatActionUseCase {

    private final CombatEngine combatEngine;
    private final GetCombatSnapshotFromStore getCombatSnapshotFromStore;
    private final CombatParticipantService combatParticipantService;
    private final CombatSessionStore combatSessionStore;
    private final CombatRepository combatRepository;

    ExecuteCombatActionUseCase(
            final CombatEngine combatEngine,
            final GetCombatSnapshotFromStore getCombatSnapshotFromStore,
            final CombatParticipantService combatParticipantService,
            final CombatSessionStore combatSessionStore, final CombatRepository combatRepository
    ) {
        this.combatEngine = combatEngine;
        this.getCombatSnapshotFromStore = getCombatSnapshotFromStore;
        this.combatParticipantService = combatParticipantService;
        this.combatSessionStore = combatSessionStore;
        this.combatRepository = combatRepository;
    }

    CombatSnapshot performAction(final CombatId combatId, final CombatAction combatAction, final AccountId accountId) {
        Combat combat = Combat.restore(
                getCombatSnapshotFromStore.getSnapshotById(combatId)
        );

        Participant participant = combatParticipantService.getCharacterByAccountId(accountId);
        combat.getParticipant(participant.getId());

        Combat result = combatEngine.handleAction(combat, combatAction, participant);

        if (result.getCombatStatus() == CombatStatus.FINISHED) {
            combatRepository.save(result).getSnapshot();
        }

        return combatSessionStore.save(result.getSnapshot());
    }
}