package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CharacterPublicDTO;
import com.arcathoria.character.vo.CharacterId;

import java.util.List;
import java.util.UUID;

public class CharacterQueryFacade {

    private final GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase;
    private final GetCharacterByIdUseCase getCharacterByIdUseCase;
    private final GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase;

    CharacterQueryFacade(
            final GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase,
            final GetCharacterByIdUseCase getCharacterByIdUseCase,
            final GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase
    ) {
        this.getAllCharactersByAccountIdUseCase = getAllCharactersByAccountIdUseCase;
        this.getCharacterByIdUseCase = getCharacterByIdUseCase;
        this.getSelectedCharacterFromCacheUseCase = getSelectedCharacterFromCacheUseCase;
    }

    public List<CharacterDTO> getAllByAccountId(final UUID uuid) {
        return CharacterDTOMapper.toCharacterDTOList(getAllCharactersByAccountIdUseCase.execute(new AccountId(uuid)));
    }

    public CharacterDTO getOwnedCharacterById(final UUID characterId, final UUID accountId) {
        return CharacterDTOMapper.toCharacterDTO(
                getCharacterByIdUseCase.getOwned(new CharacterId(characterId), new AccountId(accountId))
        );
    }

    public CharacterDTO getSelectedCharacter(final UUID accountId) {
        return CharacterDTOMapper.toCharacterDTO(
                getSelectedCharacterFromCacheUseCase.execute(new AccountId(accountId))
        );
    }

    public CharacterPublicDTO getPublicCharacterById(final UUID characterId) {
        return CharacterDTOMapper.toCharacterPublicDTO(
                getCharacterByIdUseCase.get(new CharacterId(characterId))
        );
    }
}
