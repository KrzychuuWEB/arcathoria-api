package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CharacterConfiguration {

    @Bean
    CreateCharacterUseCase createCharacterUseCase(
            final AccountQueryFacade accountQueryFacade,
            final CheckCharacterNameIsExistsUseCase checkCharacterNameIsExistsUseCase,
            final CharacterFactory characterFactory,
            final CharacterRepository characterRepository
    ) {
        return new CreateCharacterUseCase(
                accountQueryFacade,
                checkCharacterNameIsExistsUseCase,
                characterFactory,
                characterRepository
        );
    }

    @Bean
    CheckCharacterNameIsExistsUseCase checkCharacterNameIsExistsUseCase(final CharacterQueryRepository characterQueryRepository) {
        return new CheckCharacterNameIsExistsUseCase(characterQueryRepository);
    }

    @Bean
    CharacterFactory characterFactory() {
        return new CharacterFactory();
    }

    @Bean
    CharacterFacade characterFacade(final CreateCharacterUseCase characterUseCase) {
        return new CharacterFacade(characterUseCase);
    }
}
