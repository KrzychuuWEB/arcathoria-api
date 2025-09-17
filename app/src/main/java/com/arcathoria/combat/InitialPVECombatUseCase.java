package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.arcathoria.combat.CombatMapper.fromMonsterDTOToParticipant;

class InitialPVECombatUseCase {

    private static final Logger log = LogManager.getLogger(InitialPVECombatUseCase.class);
    private final CombatEngine combatEngine;
    private final CombatParticipantService combatParticipantService;
    private final MonsterClient monsterClient;
    private final CombatSessionStore combatSessionStore;

    InitialPVECombatUseCase(
            final CombatEngine combatEngine,
            final CombatParticipantService combatParticipantService,
            final MonsterClient monsterClient,
            final CombatSessionStore combatSessionStore
    ) {
        this.combatEngine = combatEngine;
        this.combatParticipantService = combatParticipantService;
        this.monsterClient = monsterClient;
        this.combatSessionStore = combatSessionStore;
    }

    CombatSnapshot init(final InitPVECombatCommand command) {
        Participant attacker = combatParticipantService.getCharacterByAccountId(new AccountId(command.attacker().id()));
        Participant defender = getMonsterByMonsterId(command.defender().id());

        Combat combat = combatEngine.initialCombat(attacker, defender, CombatType.PVE);

        return combatSessionStore.save(combat.getSnapshot());
    }

    private Participant getMonsterByMonsterId(final UUID monsterId) {
        try {
            return fromMonsterDTOToParticipant(monsterClient.getMonsterById(monsterId));
        } catch (MonsterNotFoundException e) {
            log.warn("Monster not found for id: {}", e.getMonsterId());
            throw new CombatParticipantUnavailableException(monsterId);
        }
    }
}
