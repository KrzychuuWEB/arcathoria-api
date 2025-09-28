package com.arcathoria.character;

import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class GetCharacterByIdUseCase {


    private static final Logger log = LogManager.getLogger(GetCharacterByIdUseCase.class);
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
                .orElseThrow(() -> {
                    log.warn("Character with id not found, id: {}", characterId.value());
                    return new CharacterNotFoundException(characterId.value().toString());
                });
    }

    Character getOwned(final CharacterId characterId, final AccountId accountId) {
        return characterOwnershipValidator.validate(get(characterId), accountId);
    }
}
