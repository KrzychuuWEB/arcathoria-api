package com.arcathoria.combat;

import com.arcathoria.combat.command.StartPVECombatCommand;
import com.arcathoria.combat.vo.Participant;

import java.util.UUID;

import static com.arcathoria.combat.CombatMapper.fromCharacterDTOToParticipant;
import static com.arcathoria.combat.CombatMapper.fromMonsterDTOToParticipant;

class StartPVECombatUseCase {

    private final CombatEngine combatEngine;
    private final CharacterClient characterClient;
    private final MonsterClient monsterClient;
    private final CombatRepository combatRepository;
    private final CombatStateRepository combatStateRepository;

    StartPVECombatUseCase(
            final CombatEngine combatEngine,
            final CharacterClient characterClient,
            final MonsterClient monsterClient,
            final CombatRepository combatRepository,
            final CombatStateRepository combatStateRepository
    ) {
        this.combatEngine = combatEngine;
        this.characterClient = characterClient;
        this.monsterClient = monsterClient;
        this.combatRepository = combatRepository;
        this.combatStateRepository = combatStateRepository;
    }

    CombatState execute(final StartPVECombatCommand command) {
        Participant attacker = getCharacterByAccountId(command.attacker().id());
        Participant defender = getMonsterByMonsterId(null);

        Combat combat = combatEngine.startCombat(attacker, defender, CombatType.PVE);

        return combatStateRepository.save(new CombatState(
                combat.getSnapshot().combatId(),
                combat.getSnapshot().attacker(),
                combat.getSnapshot().defender(),
                combat.getSnapshot().combatTurn().getCurrent()
        ));
    }

    private Participant getCharacterByAccountId(final UUID accountId) {
        return fromCharacterDTOToParticipant(characterClient.getSelectedCharacterByAccountId(accountId));
    }

    private Participant getMonsterByMonsterId(final UUID monsterId) {
        return fromMonsterDTOToParticipant(monsterClient.getMonsterById(null));
    }
}
