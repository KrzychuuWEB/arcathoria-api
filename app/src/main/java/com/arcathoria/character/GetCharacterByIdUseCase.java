package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.vo.CharacterId;

class GetCharacterByIdUseCase {

    private final CharacterQueryRepository characterQueryRepository;
    private final CharacterOwnershipValidator characterOwnershipValidator;

    GetCharacterByIdUseCase(
            final CharacterQueryRepository characterQueryRepository,
            final CharacterOwnershipValidator characterOwnershipValidator
    ) {
        this.characterQueryRepository = characterQueryRepository;
        this.characterOwnershipValidator = characterOwnershipValidator;
    }

    Character get(final CharacterId characterId) {
        return characterQueryRepository.getById(characterId)
                .orElseThrow(() -> new CharacterNotFoundException(characterId.value().toString()));
    }

    Character getOwned(final CharacterId characterId, final AccountId accountId) {
        return characterOwnershipValidator.validate(get(characterId), accountId);
    }
}
