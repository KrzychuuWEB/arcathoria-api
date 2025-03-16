package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.vo.CharacterName;

class CreateCharacterUseCase {

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

    Character execute(final CreateCharacterDTO dto, final AccountId accountId) {
        AccountDTO account = accountQueryFacade.getById(accountId.value());
        CharacterName characterName = new CharacterName(dto.characterName());

        checkCharacterNameIsExistsUseCase.execute(characterName);

        return characterRepository.save(
                characterFactory.createCharacter(new AccountId(account.id()), characterName)
        );
    }
}
