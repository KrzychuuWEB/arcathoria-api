package com.arcathoria.combat;

import com.arcathoria.combat.exception.OnlyOneActiveCombatAllowedException;
import com.arcathoria.combat.vo.ParticipantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OnlyOneActiveCombatPolicyTest {

    private CombatSessionStore combatSessionStore;
    private OnlyOneActiveCombatPolicy onlyOneActiveCombatPolicy;

    @BeforeEach
    void setup() {
        this.combatSessionStore = new FakeCombatSessionPort();
        this.onlyOneActiveCombatPolicy = new OnlyOneActiveCombatPolicy(this.combatSessionStore);
    }

    @Test
    void should_not_throw_exception_when_participant_is_not_in_combat() {
        assertDoesNotThrow(() -> onlyOneActiveCombatPolicy.ensureNoneActiveFor(new ParticipantId(UUID.randomUUID())));
    }

    @Test
    void should_return_OnlyOneActiveCombatAllowedException_when_participant_is_in_combat() {
        ParticipantSnapshot participantSnapshot = ParticipantSnapshotMother.aParticipantBuilder().build();
        CombatSnapshot snapshot = CombatSnapshotMother.aCombat().withAttacker(participantSnapshot).build();

        combatSessionStore.save(snapshot);

        assertThatThrownBy(() -> onlyOneActiveCombatPolicy.ensureNoneActiveFor(participantSnapshot.participantId()))
                .isInstanceOf(OnlyOneActiveCombatAllowedException.class);
    }
}