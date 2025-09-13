package com.arcathoria.combat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExecuteCombatActionUseCaseTest {

    @Mock
    private CombatEngine combatEngine;

    @Mock
    private GetCombatSnapshotFromStore getCombatSnapshotFromStore;

    @Mock
    private CharacterClient characterClient;

    @Mock
    private CombatSessionStore combatSessionStore;

    @Mock
    private MeleeCombatActionStrategy meleeCombatActionStrategy;

    @InjectMocks
    private ExecuteCombatActionUseCase executeCombatActionUseCase;

    @Test
    void should() {

    }
}