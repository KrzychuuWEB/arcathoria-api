package com.arcathoria.combat;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.combat.command.ExecuteActionCommand;
import com.arcathoria.combat.command.InitPVECombatCommand;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.vo.CombatId;
import com.arcathoria.monster.dto.MonsterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.arcathoria.combat.CombatMapper.fromParticipantToCharacterDTO;
import static com.arcathoria.combat.CombatMapper.fromParticipantToMonsterDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CombatFacadeTest {

    @Mock
    private InitialPVECombatUseCase initialPVECombatUseCase;

    @Mock
    private ExecuteCombatActionUseCase executeCombatActionUseCase;

    @InjectMocks
    private CombatFacade combatFacade;

    @Test
    void should_return_combatResultDTO_after_initial_combat() {
        UUID combatId = UUID.randomUUID();
        CharacterDTO characterDTO = fromParticipantToCharacterDTO(Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build()));
        MonsterDTO monsterDTO = fromParticipantToMonsterDTO(Participant.restore(ParticipantSnapshotMother.aParticipantBuilder().build()));
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().withCombatId(new CombatId(combatId)).build();

        when(initialPVECombatUseCase.init(any(InitPVECombatCommand.class))).thenReturn(snapshot);

        InitPVECombatCommand command = new InitPVECombatCommand(characterDTO, monsterDTO);
        CombatResultDTO result = combatFacade.initPVECombat(command);

        assertThat(result.combatId()).isEqualTo(combatId);
    }

    @Test
    void should_return_combatResultDTO_when_perform_action_in_combat() {
        UUID combatId = UUID.randomUUID();
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().withCombatId(new CombatId(combatId)).build();

        when(executeCombatActionUseCase.performAction(any(ExecuteActionCommand.class))).thenReturn(snapshot);

        ExecuteActionCommand command = new ExecuteActionCommand(new CombatId(combatId), new AccountId(UUID.randomUUID()), ActionType.MELEE);
        CombatResultDTO result = combatFacade.performActionInCombat(command);

        assertThat(result.combatId()).isEqualTo(combatId);
    }
}