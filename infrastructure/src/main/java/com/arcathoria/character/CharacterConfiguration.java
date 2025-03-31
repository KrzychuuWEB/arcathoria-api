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
    GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase(
            final CharacterQueryRepository characterQueryRepository,
            final AccountQueryFacade accountQueryFacade
    ) {
        return new GetAllCharactersByAccountIdUseCase(characterQueryRepository, accountQueryFacade);
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
    CharacterQueryFacade characterQueryFacade(
            final GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase,
            final GetCharacterByIdUseCase getCharacterByIdUseCase
    ) {
        return new CharacterQueryFacade(getAllCharactersByAccountIdUseCase, getCharacterByIdUseCase);
    }

    @Bean
    CharacterFacade characterFacade(
            final CreateCharacterUseCase characterUseCase,
            final SelectCharacterUseCase selectCharacterUseCase
    ) {
        return new CharacterFacade(characterUseCase, selectCharacterUseCase);
    }
}
