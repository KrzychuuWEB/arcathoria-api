package com.arcathoria.character;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CharacterConfiguration {

    @Bean
    CreateCharacterUseCase createCharacterUseCase(
            final AccountClient accountClient,
            final CheckCharacterNameIsExistsUseCase checkCharacterNameIsExistsUseCase,
            final CharacterFactory characterFactory,
            final CharacterRepository characterRepository
    ) {
        return new CreateCharacterUseCase(
                accountClient,
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
    GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase(
            final CharacterQueryRepository characterQueryRepository,
            final AccountClient accountClient
    ) {
        return new GetAllCharactersByAccountIdUseCase(characterQueryRepository, accountClient);
    }

    @Bean
    CharacterOwnershipValidator ownershipValidator() {
        return new CharacterOwnershipValidator();
    }

    @Bean
    SelectCharacterUseCase selectCharacterUseCase(
            final GetCharacterByIdUseCase getCharacterByIdUseCase,
            final SelectCharacterCachePort selectCharacterCachePort
    ) {
        return new SelectCharacterUseCase(getCharacterByIdUseCase, selectCharacterCachePort);
    }

    @Bean
    GetCharacterByIdUseCase getCharacterByIdUseCase(
            final CharacterQueryRepository characterQueryRepository,
            final CharacterOwnershipValidator ownershipValidator
    ) {
        return new GetCharacterByIdUseCase(characterQueryRepository, ownershipValidator);
    }

    @Bean
    GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase(
            final GetCharacterByIdUseCase getCharacterByIdUseCase,
            final SelectCharacterCachePort selectCharacterCachePort
    ) {
        return new GetSelectedCharacterFromCacheUseCase(getCharacterByIdUseCase, selectCharacterCachePort);
    }

    @Bean
    RemoveSelectedCharacterUseCase removeSelectedCharacterUseCase(
            final GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase,
            final SelectCharacterCachePort selectCharacterCachePort
    ) {
        return new RemoveSelectedCharacterUseCase(selectCharacterCachePort, getSelectedCharacterFromCacheUseCase);
    }

    @Bean
    CharacterQueryFacade characterQueryFacade(
            final GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase,
            final GetCharacterByIdUseCase getCharacterByIdUseCase,
            final GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase
    ) {
        return new CharacterQueryFacade(getAllCharactersByAccountIdUseCase, getCharacterByIdUseCase, getSelectedCharacterFromCacheUseCase);
    }

    @Bean
    CharacterFacade characterFacade(
            final CreateCharacterUseCase characterUseCase,
            final SelectCharacterUseCase selectCharacterUseCase,
            final RemoveSelectedCharacterUseCase removeSelectedCharacterUseCase
    ) {
        return new CharacterFacade(characterUseCase, selectCharacterUseCase, removeSelectedCharacterUseCase);
    }
}
