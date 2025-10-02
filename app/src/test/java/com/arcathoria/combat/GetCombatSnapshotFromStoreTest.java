package com.arcathoria.combat;

import com.arcathoria.combat.exception.CombatNotFoundDomainException;
import com.arcathoria.combat.vo.CombatId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCombatSnapshotFromStoreTest {

    @Mock
    private CombatSessionStore combatSessionStore;

    @InjectMocks
    private GetCombatSnapshotFromStore getCombatSnapshotFromStore;

    @Test
    void should_return_combat_snapshot_by_id() {
        CombatId combatId = new CombatId(UUID.randomUUID());
        CombatSnapshot combatSnapshot = CombatSnapshotMother.aCombat().withCombatId(combatId).build();

        when(combatSessionStore.getCombatById(combatId)).thenReturn(Optional.of(combatSnapshot));

        CombatSnapshot result = getCombatSnapshotFromStore.getSnapshotById(combatId);

        assertThat(result.combatId()).isEqualTo(combatId);
        assertThat(result.combatStatus()).isEqualTo(combatSnapshot.combatStatus());
        assertThat(result.attacker()).isEqualTo(combatSnapshot.attacker());
        assertThat(result.defender()).isEqualTo(combatSnapshot.defender());

        verify(combatSessionStore).getCombatById(combatId);
    }

    @Test
    void should_return_CombatNotFoundException_if_combat_id_not_exists() {
        when(combatSessionStore.getCombatById(any(CombatId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getCombatSnapshotFromStore.getSnapshotById(new CombatId(UUID.randomUUID())))
                .isInstanceOf(CombatNotFoundDomainException.class)
                .hasMessageContaining("Combat not found");
    }
}