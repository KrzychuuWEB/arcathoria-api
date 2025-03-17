package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;

import java.util.List;
import java.util.UUID;

class CharacterQueryFacade {

    private final GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase;

    CharacterQueryFacade(
            final GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase
    ) {
        this.getAllCharactersByAccountIdUseCase = getAllCharactersByAccountIdUseCase;
    }

    List<CharacterDTO> getAllByAccountId(UUID uuid) {
        return CharacterDTOMapper.toCharacterDTOList(getAllCharactersByAccountIdUseCase.execute(new AccountId(uuid)));
    }
}
