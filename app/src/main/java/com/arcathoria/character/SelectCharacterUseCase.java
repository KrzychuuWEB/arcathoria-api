package com.arcathoria.character;

import com.arcathoria.character.command.SelectCharacterCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class SelectCharacterUseCase {


    private static final Logger log = LogManager.getLogger(SelectCharacterUseCase.class);
    private final GetCharacterByIdUseCase getCharacterByIdUseCase;
    private final SelectCharacterCachePort selectCharacterCachePort;

    SelectCharacterUseCase(final GetCharacterByIdUseCase getCharacterByIdUseCase,
                           final SelectCharacterCachePort selectCharacterCachePort
    ) {
        this.getCharacterByIdUseCase = getCharacterByIdUseCase;
        this.selectCharacterCachePort = selectCharacterCachePort;
    }

    Character execute(final SelectCharacterCommand command) {
        Character character = getCharacterByIdUseCase.getOwned(command.characterId(), command.accountId());

        selectCharacterCachePort.setValueAndSetExpiredTime(command.characterId(), command.accountId());

        log.info("Select character {} for account {}",
                command.characterId().value(),
                command.accountId().value()
        );

        return character;
    }
}
