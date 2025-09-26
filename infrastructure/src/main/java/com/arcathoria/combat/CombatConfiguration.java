package com.arcathoria.combat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CombatConfiguration {

    @Bean
    CombatQueryFacade combatQueryFacade(final GetActiveCombatByParticipantId getActiveCombatByParticipantId,
                                        final CombatParticipantService combatParticipantService) {
        return new CombatQueryFacade(getActiveCombatByParticipantId, combatParticipantService);
    }

    @Bean
    GetActiveCombatByParticipantId getActiveCombatByParticipantId(final CombatSessionStore combatSessionStore) {
        return new GetActiveCombatByParticipantId(combatSessionStore);
    }

    @Bean
    CombatFacade combatFacade(
            final InitialPVECombatUseCase initialPVECombatUseCase,
            final ExecuteCombatActionUseCase executeCombatActionUseCase
    ) {
        return new CombatFacade(initialPVECombatUseCase, executeCombatActionUseCase);
    }

    @Bean
    InitialPVECombatUseCase initialPVECombatUseCase(
            final CombatEngine combatEngine,
            final CombatParticipantService combatParticipantService,
            final MonsterClient monsterClient,
            final CombatSessionStore sessionStore
    ) {
        return new InitialPVECombatUseCase(
                combatEngine,
                combatParticipantService,
                monsterClient,
                sessionStore
        );
    }

    @Bean
    ExecuteCombatActionUseCase executeCombatActionUseCase(
            final CombatEngine combatEngine,
            final GetCombatSnapshotFromStore getCombatSnapshotFromStore,
            final CombatParticipantService combatParticipantService,
            final CombatSessionStore combatSessionStore,
            final CombatRepository repository,
            final CombatActionRegistry registry
    ) {
        return new ExecuteCombatActionUseCase(
                combatEngine,
                getCombatSnapshotFromStore,
                combatParticipantService,
                combatSessionStore,
                repository,
                registry
        );
    }

    @Bean
    CombatEngine combatEngine(
            final CombatFactory combatFactory,
            final CombatSideStrategyFactory combatSideStrategyFactory
    ) {
        return new CombatEngine(combatFactory, combatSideStrategyFactory);
    }

    @Bean
    GetCombatSnapshotFromStore getCombatSnapshotFromStore(
            final CombatSessionStore combatSessionStore
    ) {
        return new GetCombatSnapshotFromStore(combatSessionStore);
    }

    @Bean
    CombatParticipantService combatParticipantService(
            final CharacterClient characterClient
    ) {
        return new CombatParticipantService(characterClient);
    }

    @Bean
    CombatFactory combatFactory() {
        return new CombatFactory();
    }

    @Bean
    DefaultCombatSideStrategyFactory combatSideStrategyFactory() {
        return new DefaultCombatSideStrategyFactory();
    }
}
