package com.arcathoria.combat;

import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.combat.command.StartPVECombatCommand;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import com.arcathoria.combat.vo.Participant;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static com.arcathoria.combat.CombatMapper.fromCharacterDTOToParticipant;
import static com.arcathoria.combat.CombatMapper.fromMonsterDTOToParticipant;

class StartPVECombatUseCase {

    private static final Logger log = LogManager.getLogger(StartPVECombatUseCase.class);
    private final CombatEngine combatEngine;
    private final CharacterClient characterClient;
    private final MonsterClient monsterClient;
    private final CombatStateRepository combatStateRepository;

    StartPVECombatUseCase(
            final CombatEngine combatEngine,
            final CharacterClient characterClient,
            final MonsterClient monsterClient,
            final CombatStateRepository combatStateRepository
    ) {
        this.combatEngine = combatEngine;
        this.characterClient = characterClient;
        this.monsterClient = monsterClient;
        this.combatStateRepository = combatStateRepository;
    }

    CombatState execute(final StartPVECombatCommand command) {
        Participant attacker = getCharacterByAccountId(command.attacker().id());
        Participant defender = getMonsterByMonsterId(command.defender().id());

        Combat combat = combatEngine.startCombat(attacker, defender, CombatType.PVE);

        return combatStateRepository.save(new CombatState(
                combat.getSnapshot().combatId(),
                combat.getSnapshot().attacker(),
                combat.getSnapshot().defender(),
                combat.getSnapshot().combatTurn().getCurrent()
        ));
    }

    private Participant getCharacterByAccountId(final UUID accountId) {
        try {
            return fromCharacterDTOToParticipant(characterClient.getSelectedCharacterByAccountId(accountId));
        } catch (CharacterNotFoundException e) {
            log.warn("Character not found for id: {}", e.getValue());
            throw new CombatParticipantUnavailableException(CombatSide.ATTACKER);
        }
    }

    private Participant getMonsterByMonsterId(final UUID monsterId) {
        try {
            return fromMonsterDTOToParticipant(monsterClient.getMonsterById(monsterId));
        } catch (MonsterNotFoundException e) {
            log.warn("Monster not found for id: {}", e.getMonsterId());
            throw new CombatParticipantUnavailableException(CombatSide.DEFENDER);
        }
    }
}
