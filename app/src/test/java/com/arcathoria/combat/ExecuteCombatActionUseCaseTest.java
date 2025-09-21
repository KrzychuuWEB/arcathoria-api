package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.combat.command.ExecuteActionCommand;
import com.arcathoria.combat.exception.CombatAlreadyFinishedException;
import com.arcathoria.combat.exception.ParticipantNotFoundInCombatException;
import com.arcathoria.combat.exception.WrongTurnException;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.combat.vo.CombatTurn;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecuteCombatActionUseCaseTest {

    @Mock
    private CombatEngine combatEngine;

    @Mock
    private GetCombatSnapshotFromStore getCombatSnapshotFromStore;

    @Mock
    private CombatSessionStore combatSessionStore;

    @Mock
    private MeleeCombatActionStrategy meleeCombatActionStrategy;

    @Mock
    private CombatParticipantService combatParticipantService;

    @Mock
    private CombatRepository combatRepository;

    @Mock
    private CombatActionRegistry combatActionRegistry;

    @InjectMocks
    private ExecuteCombatActionUseCase executeCombatActionUseCase;

    @Test
    void should_save_combat_snapshot_to_cache_after_perform_action() {
        CombatId combatId = new CombatId(UUID.randomUUID());
        ParticipantId participantPerformActionId = new ParticipantId(UUID.randomUUID());
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(participantPerformActionId).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).build());

        when(getCombatSnapshotFromStore.getSnapshotById(any(CombatId.class))).thenReturn(combat.getSnapshot());
        when(combatParticipantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(attacker);
        when(combatActionRegistry.get(any(ActionType.class))).thenReturn(meleeCombatActionStrategy);
        when(combatEngine.handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class))).thenReturn(combat);
        when(combatSessionStore.save(any(CombatSnapshot.class))).thenReturn(combat.getSnapshot());

        ExecuteActionCommand command = new ExecuteActionCommand(combatId, new AccountId(UUID.randomUUID()), ActionType.MELEE);
        CombatSnapshot result = executeCombatActionUseCase.performAction(command);

        assertThat(result).isEqualTo(combat.getSnapshot());
        assertThat(result.combatStatus()).isEqualTo(CombatStatus.IN_PROGRESS);

        verify(getCombatSnapshotFromStore).getSnapshotById(combatId);
        verify(combatParticipantService).getCharacterByAccountId(any(AccountId.class));

        ArgumentCaptor<Combat> combatArgumentCaptor = ArgumentCaptor.forClass(Combat.class);
        ArgumentCaptor<Participant> participantArgumentCaptor = ArgumentCaptor.forClass(Participant.class);

        verify(combatEngine).handleAction(combatArgumentCaptor.capture(), any(CombatAction.class), participantArgumentCaptor.capture());

        assertThat(combatArgumentCaptor.getValue().getSnapshot()).isEqualTo(combat.getSnapshot());
        assertThat(participantArgumentCaptor.getValue().getId()).isEqualTo(attacker.getId());

        verify(combatRepository, times(0)).save(any(Combat.class));
    }

    @Test
    void should_throw_exception_when_participant_not_found_in_combat() {
        CombatId combatId = new CombatId(UUID.randomUUID());
        Participant attackerWithBadId = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(new ParticipantId(UUID.randomUUID())).build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).build());

        when(getCombatSnapshotFromStore.getSnapshotById(any(CombatId.class))).thenReturn(combat.getSnapshot());
        when(combatParticipantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(attackerWithBadId);

        ExecuteActionCommand command = new ExecuteActionCommand(combatId, new AccountId(UUID.randomUUID()), ActionType.MELEE);
        assertThatThrownBy(() -> executeCombatActionUseCase.performAction(command))
                .isInstanceOf(ParticipantNotFoundInCombatException.class);

        verify(getCombatSnapshotFromStore).getSnapshotById(any(CombatId.class));
        verify(combatParticipantService).getCharacterByAccountId(any(AccountId.class));
        verify(combatEngine, times(0)).handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class));
        verify(combatSessionStore, times(0)).save(any(CombatSnapshot.class));
        verify(combatRepository, times(0)).save(any(Combat.class));
    }

    @Test
    void should_save_combat_to_repository_after_action_if_combat_status_is_finish() {
        CombatId combatId = new CombatId(UUID.randomUUID());
        ParticipantId participantPerformActionId = new ParticipantId(UUID.randomUUID());
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(participantPerformActionId).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).build());
        Combat finishedCombat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).withCombatStatus(CombatStatus.FINISHED).build());

        when(getCombatSnapshotFromStore.getSnapshotById(any(CombatId.class))).thenReturn(combat.getSnapshot());
        when(combatParticipantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(attacker);
        when(combatActionRegistry.get(any(ActionType.class))).thenReturn(meleeCombatActionStrategy);
        when(combatEngine.handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class))).thenReturn(finishedCombat);
        when(combatRepository.save(any(Combat.class))).thenReturn(finishedCombat);
        when(combatSessionStore.save(any(CombatSnapshot.class))).thenReturn(finishedCombat.getSnapshot());

        ExecuteActionCommand command = new ExecuteActionCommand(combatId, new AccountId(UUID.randomUUID()), ActionType.MELEE);
        CombatSnapshot result = executeCombatActionUseCase.performAction(command);

        assertThat(result).isEqualTo(finishedCombat.getSnapshot());
        assertThat(result.combatStatus()).isEqualTo(CombatStatus.FINISHED);
        assertThat(result.attacker().participantId()).isEqualTo(participantPerformActionId);

        verify(getCombatSnapshotFromStore).getSnapshotById(combatId);
        verify(combatParticipantService).getCharacterByAccountId(any(AccountId.class));

        ArgumentCaptor<Combat> combatArgumentCaptor = ArgumentCaptor.forClass(Combat.class);
        ArgumentCaptor<CombatSnapshot> combatSnapshottArgumentCaptor = ArgumentCaptor.forClass(CombatSnapshot.class);
        ArgumentCaptor<Participant> participantArgumentCaptor = ArgumentCaptor.forClass(Participant.class);

        verify(combatEngine).handleAction(combatArgumentCaptor.capture(), any(CombatAction.class), participantArgumentCaptor.capture());
        assertThat(combatArgumentCaptor.getValue().getSnapshot()).isEqualTo(combat.getSnapshot());
        assertThat(participantArgumentCaptor.getValue().getId()).isEqualTo(attacker.getId());

        verify(combatRepository).save(combatArgumentCaptor.capture());
        assertThat(combatArgumentCaptor.getValue().getSnapshot()).isEqualTo(finishedCombat.getSnapshot());
        assertThat(combatArgumentCaptor.getValue().getCombatStatus()).isEqualTo(CombatStatus.FINISHED);

        verify(combatSessionStore).save(combatSnapshottArgumentCaptor.capture());
        assertThat(combatSnapshottArgumentCaptor.getValue()).isEqualTo(finishedCombat.getSnapshot());
    }

    @Test
    void should_return_WrongTurnException_when_current_turn_has_opponent() {
        CombatId combatId = new CombatId(UUID.randomUUID());
        ParticipantId participantPerformActionId = new ParticipantId(UUID.randomUUID());
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(participantPerformActionId).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).withCombatTurn(new CombatTurn(CombatSide.DEFENDER)).build());

        when(getCombatSnapshotFromStore.getSnapshotById(any(CombatId.class))).thenReturn(combat.getSnapshot());
        when(combatParticipantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(attacker);
        when(combatActionRegistry.get(any(ActionType.class))).thenReturn(meleeCombatActionStrategy);
        when(combatEngine.handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class))).thenThrow(WrongTurnException.class);

        ExecuteActionCommand command = new ExecuteActionCommand(combatId, new AccountId(UUID.randomUUID()), ActionType.MELEE);
        assertThatThrownBy(() -> executeCombatActionUseCase.performAction(command))
                .isInstanceOf(WrongTurnException.class);

        verify(getCombatSnapshotFromStore).getSnapshotById(any(CombatId.class));
        verify(combatParticipantService).getCharacterByAccountId(any(AccountId.class));
        verify(combatEngine).handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class));
        verify(combatSessionStore, times(0)).save(any(CombatSnapshot.class));
        verify(combatRepository, times(0)).save(any(Combat.class));
    }

    @Test
    void should_return_CombatAlreadyFinishedException_when_combat_is_finished_before_perform_action() {
        CombatId combatId = new CombatId(UUID.randomUUID());
        ParticipantId participantPerformActionId = new ParticipantId(UUID.randomUUID());
        Participant attacker = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().withId(participantPerformActionId).build());
        Participant defender = Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build());
        Combat combat = Combat.restore(CombatSnapshotMother.aCombat().withCombatId(combatId).withAttacker(attacker.getSnapshot()).withDefender(defender.getSnapshot()).withCombatStatus(CombatStatus.FINISHED).build());

        when(getCombatSnapshotFromStore.getSnapshotById(any(CombatId.class))).thenReturn(combat.getSnapshot());
        when(combatParticipantService.getCharacterByAccountId(any(AccountId.class))).thenReturn(attacker);
        when(combatActionRegistry.get(any(ActionType.class))).thenReturn(meleeCombatActionStrategy);
        when(combatEngine.handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class))).thenThrow(CombatAlreadyFinishedException.class);

        ExecuteActionCommand command = new ExecuteActionCommand(combatId, new AccountId(UUID.randomUUID()), ActionType.MELEE);
        assertThatThrownBy(() -> executeCombatActionUseCase.performAction(command))
                .isInstanceOf(CombatAlreadyFinishedException.class);

        verify(getCombatSnapshotFromStore).getSnapshotById(any(CombatId.class));
        verify(combatParticipantService).getCharacterByAccountId(any(AccountId.class));
        verify(combatEngine).handleAction(any(Combat.class), any(CombatAction.class), any(Participant.class));
        verify(combatSessionStore, times(0)).save(any(CombatSnapshot.class));
        verify(combatRepository, times(0)).save(any(Combat.class));
    }
}