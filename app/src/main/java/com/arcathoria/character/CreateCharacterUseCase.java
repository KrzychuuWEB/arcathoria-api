package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.character.command.CreateCharacterCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CreateCharacterUseCase {


    private static final Logger log = LogManager.getLogger(CreateCharacterUseCase.class);
    private final AccountQueryFacade accountQueryFacade;
    private final CheckCharacterNameIsExistsUseCase checkCharacterNameIsExistsUseCase;
    private final CharacterFactory characterFactory;
    private final CharacterRepository characterRepository;

    CreateCharacterUseCase(
            final AccountQueryFacade accountQueryFacade,
            final CheckCharacterNameIsExistsUseCase checkCharacterNameIsExistsUseCase,
            final CharacterFactory characterFactory,
            final CharacterRepository characterRepository
    ) {
        this.accountQueryFacade = accountQueryFacade;
        this.checkCharacterNameIsExistsUseCase = checkCharacterNameIsExistsUseCase;
        this.characterFactory = characterFactory;
        this.characterRepository = characterRepository;
    }

    Character execute(final CreateCharacterCommand command) {
        accountQueryFacade.getById(command.accountId().value());

        checkCharacterNameIsExistsUseCase.execute(command.characterName());

        Character character = characterRepository.save(
                characterFactory.createCharacter(command.accountId(), command.characterName())
        );

        log.info("Character created with id {}", character.getSnapshot().getCharacterId().value());

        return character;
    }
}
