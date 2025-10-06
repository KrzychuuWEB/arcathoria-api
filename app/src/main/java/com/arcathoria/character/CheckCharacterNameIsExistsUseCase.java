package com.arcathoria.character;

import com.arcathoria.character.exception.CharacterNameExistsException;
import com.arcathoria.character.vo.CharacterName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CheckCharacterNameIsExistsUseCase {

    private static final Logger logger = LogManager.getLogger(CheckCharacterNameIsExistsUseCase.class);
    private final CharacterQueryRepository characterQueryRepository;

    CheckCharacterNameIsExistsUseCase(final CharacterQueryRepository characterQueryRepository) {
        this.characterQueryRepository = characterQueryRepository;
    }

    void execute(final CharacterName characterName) {
        if (characterQueryRepository.existsByName(characterName)) {
            logger.warn("Character name already exists: {}", characterName.value());
            throw new CharacterNameExistsException(characterName);
        }
    }
}
