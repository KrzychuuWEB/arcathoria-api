package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.vo.CombatId;

class ExecuteCombatActionUseCase {

    private final CombatEngine combatEngine;
    private final GetCombatSnapshotFromStore getCombatSnapshotFromStore;
    private final CombatParticipantService combatParticipantService;
    private final CombatSessionStore combatSessionStore;

    ExecuteCombatActionUseCase(
            final CombatEngine combatEngine,
            final GetCombatSnapshotFromStore getCombatSnapshotFromStore,
            final CombatParticipantService combatParticipantService,
            final CombatSessionStore combatSessionStore
    ) {
        this.combatEngine = combatEngine;
        this.getCombatSnapshotFromStore = getCombatSnapshotFromStore;
        this.combatParticipantService = combatParticipantService;
        this.combatSessionStore = combatSessionStore;
    }

    CombatSnapshot performAction(final CombatId combatId, final CombatAction combatAction, final AccountId accountId) {
        Combat combat = Combat.restore(
                getCombatSnapshotFromStore.getSnapshotById(combatId)
        );

        Participant participant = combatParticipantService.getCharacterByAccountId(accountId.value());

        combatEngine.handleAction(combat, combatAction, participant);

        return combatSessionStore.save(combat.getSnapshot());
    }
}