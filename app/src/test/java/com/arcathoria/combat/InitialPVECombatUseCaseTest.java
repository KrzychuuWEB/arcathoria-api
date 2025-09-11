package com.arcathoria.combat;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.combat.command.StartPVECombatCommand;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitialPVECombatUseCaseTest {

    @Mock
    private CombatEngine combatEngine;

    @Mock
    private CharacterClient characterClient;

    @Mock
    private MonsterClient monsterClient;

    @Mock
    private CombatSessionStore combatSessionStore;

    @InjectMocks
    private InitialPVECombatUseCase initialPVECombatUseCase;

    @Test
    void should_start_combat_and_persist_state_when_valid_command_given() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).build());

        CharacterDTO player = new CharacterDTO(attacker.getId().value(), "example-player", attacker.getHealth().getMax(), attacker.getIntelligenceLevel());
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        StartPVECombatCommand command = new StartPVECombatCommand(
                player,
                monster
        );

        when(characterClient.getSelectedCharacterByAccountId(player.id())).thenReturn(player);
        when(monsterClient.getMonsterById(monster.id())).thenReturn(monster);
        when(combatEngine.initialCombat(attacker, defender, CombatType.PVE)).thenReturn(combat);

        initialPVECombatUseCase.execute(command);

        verify(characterClient).getSelectedCharacterByAccountId(player.id());
        verify(monsterClient).getMonsterById(monster.id());
        verify(combatEngine).initialCombat(attacker, defender, CombatType.PVE);
        verify(combatSessionStore).save(CombatSnapshotMother.aCombat().withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).build());
    }

    @Test
    void should_throw_when_character_not_found() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        CharacterDTO player = new CharacterDTO(attacker.getId().value(), "example-player", attacker.getHealth().getMax(), attacker.getIntelligenceLevel());
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        when(characterClient.getSelectedCharacterByAccountId(player.id())).thenThrow(CharacterNotFoundException.class);

        assertThatThrownBy(() -> initialPVECombatUseCase.execute(new StartPVECombatCommand(player, monster)))
                .isInstanceOf(CombatParticipantUnavailableException.class)
                .hasMessage("Could not retrieve attacker participant for combat.");
    }

    @Test
    void should_throw_when_monster_not_found() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        CharacterDTO player = new CharacterDTO(attacker.getId().value(), "example-player", attacker.getHealth().getMax(), attacker.getIntelligenceLevel());
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        when(characterClient.getSelectedCharacterByAccountId(player.id())).thenReturn(player);
        when(monsterClient.getMonsterById(monster.id())).thenThrow(MonsterNotFoundException.class);

        assertThatThrownBy(() -> initialPVECombatUseCase.execute(new StartPVECombatCommand(player, monster)))
                .isInstanceOf(CombatParticipantUnavailableException.class)
                .hasMessage("Could not retrieve defender participant for combat.");
    }
}