package com.arcathoria.combat;

import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableDomainException;
import com.arcathoria.combat.exception.OnlyOneActiveCombatAllowedDomainException;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        ParticipantView monster = ParticipantViewMother.aParticipantBuilder().withId(defender.getId().value()).withParticipantType(ParticipantType.MONSTER).build();

        AccountId accountId = new AccountId(attacker.getId().value());
        MonsterId monsterId = new MonsterId(defender.getId().value());

        InitPVECombatCommand command = new InitPVECombatCommand(accountId, monsterId);

        when(combatParticipantService.getCharacterByAccountId(new AccountId(accountId.value()))).thenReturn(attacker);
        when(monsterClient.getMonsterById(monsterId)).thenReturn(monster);
        when(combatEngine.initialCombat(attacker, defender, CombatType.PVE)).thenReturn(combat);

        initialPVECombatUseCase.init(command);

        verify(combatParticipantService).getCharacterByAccountId(new AccountId(accountId.value()));
        verify(monsterClient).getMonsterById(monsterId);
        verify(combatEngine).initialCombat(attacker, defender, CombatType.PVE);
        verify(combatSessionStore).save(CombatSnapshotMother.aCombat().withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).build());
    }

    @Test
    void should_throw_when_character_not_found() {
        ParticipantView player = ParticipantViewMother.aParticipantBuilder().withParticipantType(ParticipantType.PLAYER).build();
        ParticipantView monster = ParticipantViewMother.aParticipantBuilder().withParticipantType(ParticipantType.MONSTER).build();

        when(combatParticipantService.getCharacterByAccountId(new AccountId(player.id()))).thenThrow(new CombatParticipantNotAvailableDomainException(new ParticipantId(player.id()), null));

        assertThatThrownBy(() -> initialPVECombatUseCase.init(new InitPVECombatCommand(new AccountId(player.id()), new MonsterId(monster.id()))))
                .isInstanceOf(CombatParticipantNotAvailableDomainException.class)
                .hasMessageContaining(player.id().toString());

        verify(combatSessionStore, never()).save(any(CombatSnapshot.class));
    }

    @Test
    void should_throw_when_monster_not_found() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        ParticipantView player = ParticipantViewMother.aParticipantBuilder().withParticipantType(ParticipantType.PLAYER).build();
        ParticipantView monster = ParticipantViewMother.aParticipantBuilder().withParticipantType(ParticipantType.MONSTER).build();

        when(combatParticipantService.getCharacterByAccountId(new AccountId(player.id()))).thenReturn(attacker);
        when(monsterClient.getMonsterById(new MonsterId(monster.id()))).thenThrow(CombatParticipantNotAvailableDomainException.class);

        assertThatThrownBy(() -> initialPVECombatUseCase.init(new InitPVECombatCommand(new AccountId(player.id()), new MonsterId(monster.id()))))
                .isInstanceOf(CombatParticipantNotAvailableDomainException.class);

        verify(combatSessionStore, never()).save(any(CombatSnapshot.class));
    }

    @Test
    void should_return_OnlyOneActiveCombatAllowedException_when_participant_has_active_combat() {
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());

        ParticipantView player = ParticipantViewMother.aParticipantBuilder().withId(attacker.getId().value()).withParticipantType(ParticipantType.PLAYER).build();
        ParticipantView monster = ParticipantViewMother.aParticipantBuilder().withId(defender.getId().value()).withParticipantType(ParticipantType.MONSTER).build();

        when(combatParticipantService.getCharacterByAccountId(new AccountId(player.id()))).thenReturn(attacker);
        when(monsterClient.getMonsterById(new MonsterId(monster.id()))).thenReturn(monster);
        when(combatEngine.initialCombat(attacker, defender, CombatType.PVE)).thenThrow(new OnlyOneActiveCombatAllowedDomainException(attacker.getId()));

        assertThatThrownBy(() -> initialPVECombatUseCase.init(new InitPVECombatCommand(new AccountId(player.id()), new MonsterId(monster.id()))))
                .isInstanceOf(OnlyOneActiveCombatAllowedDomainException.class)
                .hasMessageContaining(attacker.getId().value().toString());

        verify(combatSessionStore, never()).save(any(CombatSnapshot.class));
        verify(combatParticipantService).getCharacterByAccountId(new AccountId(player.id()));
        verify(monsterClient).getMonsterById(new MonsterId(monster.id()));
    }
}