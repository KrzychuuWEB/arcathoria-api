package com.arcathoria.monster;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MonsterQueryConfig {

    @Bean
    GetMonsterByIdUseCase getMonsterByIdUseCase(
            final MonsterQueryRepository monsterQueryRepository
    ) {
        return new GetMonsterByIdUseCase(monsterQueryRepository);
    }

    @Bean
    MonsterQueryFacade monsterQueryFacade(
            final GetMonsterByIdUseCase getMonsterByIdUseCase
    ) {
        return new MonsterQueryFacade(getMonsterByIdUseCase);
    }
}
