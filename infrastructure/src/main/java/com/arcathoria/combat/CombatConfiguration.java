package com.arcathoria.combat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CombatConfiguration {

    @Bean
    CombatQueryFacade combatQueryFacade(final GetActiveCombatIdByParticipantId getActiveCombatIdByParticipantId,
                                        final CombatParticipantService combatParticipantService,
                                        final GetCombatFromStoreByIdAndParticipantId getCombatFromStoreByIdAndParticipantId
    ) {
        return new CombatQueryFacade(getActiveCombatIdByParticipantId, combatParticipantService, getCombatFromStoreByIdAndParticipantId);
    }

    @Bean
    GetActiveCombatIdByParticipantId getActiveCombatByParticipantId(final CombatSessionStore combatSessionStore) {
        return new GetActiveCombatIdByParticipantId(combatSessionStore);
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
    GetCombatFromStoreByIdAndParticipantId getCombatFromStoreByIdAndParticipantId(
            final GetCombatSnapshotFromStore getCombatSnapshotFromStore
    ) {
        return new GetCombatFromStoreByIdAndParticipantId(getCombatSnapshotFromStore);
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
            final CombatSideStrategyFactory combatSideStrategyFactory,
            final OnlyOneActiveCombatPolicy onlyOneActiveCombatPolicy
    ) {
        return new CombatEngine(combatFactory, combatSideStrategyFactory, onlyOneActiveCombatPolicy);
    }

    @Bean
    OnlyOneActiveCombatPolicy onlyOneActiveCombatPolicy(final CombatSessionStore combatSessionStore) {
        return new OnlyOneActiveCombatPolicy(combatSessionStore);
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
