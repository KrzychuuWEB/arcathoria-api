package com.arcathoria.combat;

import com.arcathoria.ApiException;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.exception.CombatParticipantUnavailableException;
import com.arcathoria.combat.exception.OnlyOneActiveCombatAllowedException;
import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterNotFoundException;
import com.arcathoria.monster.vo.MonsterId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitialPVECombatUseCaseTest {

    @Mock
    private CombatEngine combatEngine;

    @Mock
    private CombatParticipantService combatParticipantService;

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
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        AccountId accountId = new AccountId(attacker.getId().value());
        MonsterId monsterId = new MonsterId(defender.getId().value());

        InitPVECombatCommand command = new InitPVECombatCommand(accountId, monsterId);

        when(combatParticipantService.getCharacterByAccountId(new AccountId(accountId.value()))).thenReturn(attacker);
        when(monsterClient.getMonsterById(monsterId.value())).thenReturn(Optional.of(monster));
        when(combatEngine.initialCombat(attacker, defender, CombatType.PVE)).thenReturn(combat);

        initialPVECombatUseCase.init(command);

        verify(combatParticipantService).getCharacterByAccountId(new AccountId(accountId.value()));
        verify(monsterClient).getMonsterById(monsterId.value());
        verify(combatEngine).initialCombat(attacker, defender, CombatType.PVE);
        verify(combatSessionStore).save(CombatSnapshotMother.aCombat().withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).build());
    }

    @Test
    void should_throw_when_character_not_found() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        CharacterDTO player = new CharacterDTO(attacker.getId().value(), "example-player", attacker.getHealth().getMax(), attacker.getIntelligenceLevel());
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        when(combatParticipantService.getCharacterByAccountId(new AccountId(player.id()))).thenThrow(CombatParticipantUnavailableException.class);

        assertThatThrownBy(() -> initialPVECombatUseCase.init(new InitPVECombatCommand(new AccountId(player.id()), new MonsterId(monster.id()))))
                .isInstanceOf(CombatParticipantUnavailableException.class);

        verify(combatSessionStore, never()).save(any(CombatSnapshot.class));
    }

    @Test
    void should_throw_when_monster_not_found() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        CharacterDTO player = new CharacterDTO(attacker.getId().value(), "example-player", attacker.getHealth().getMax(), attacker.getIntelligenceLevel());
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        when(combatParticipantService.getCharacterByAccountId(new AccountId(player.id()))).thenReturn(attacker);
        when(monsterClient.getMonsterById(monster.id())).thenThrow(MonsterNotFoundException.class);

        assertThatThrownBy(() -> initialPVECombatUseCase.init(new InitPVECombatCommand(new AccountId(player.id()), new MonsterId(monster.id()))))
                .isInstanceOf(ApiException.class);

        verify(combatSessionStore, never()).save(any(CombatSnapshot.class));
    }

    @Test
    void should_return_OnlyOneActiveCombatAllowedException_when_participant_has_active_combat() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        CharacterDTO player = new CharacterDTO(attacker.getId().value(), "example-player", attacker.getHealth().getMax(), attacker.getIntelligenceLevel());
        MonsterDTO monster = new MonsterDTO(defender.getId().value(), "example-monster", defender.getHealth().getMax(), defender.getHealth().getMax(), defender.getIntelligenceLevel());

        when(combatParticipantService.getCharacterByAccountId(new AccountId(player.id()))).thenReturn(attacker);
        when(monsterClient.getMonsterById(monster.id())).thenReturn(Optional.of(monster));
        when(combatEngine.initialCombat(attacker, defender, CombatType.PVE)).thenThrow(new OnlyOneActiveCombatAllowedException(attacker.getId()));

        assertThatThrownBy(() -> initialPVECombatUseCase.init(new InitPVECombatCommand(new AccountId(player.id()), new MonsterId(monster.id()))))
                .isInstanceOf(OnlyOneActiveCombatAllowedException.class)
                .hasMessageContaining(attacker.getId().value().toString());

        verify(combatSessionStore, never()).save(any(CombatSnapshot.class));
        verify(combatParticipantService).getCharacterByAccountId(new AccountId(player.id()));
        verify(monsterClient).getMonsterById(monster.id());
    }
}