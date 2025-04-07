package com.arcathoria.character;

import com.arcathoria.character.command.SelectCharacterCommand;

class SelectCharacterUseCase {

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

        return character;
    }
}
