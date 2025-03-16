package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;

import java.util.UUID;

import static com.arcathoria.character.CharacterDTOMapper.toCharacterDTO;

public class CharacterFacade {

    private final CreateCharacterUseCase characterUseCase;

    CharacterFacade(final CreateCharacterUseCase characterUseCase) {
        this.characterUseCase = characterUseCase;
    }

    public CharacterDTO createCharacter(final CreateCharacterDTO dto, final UUID id) {
        return toCharacterDTO(characterUseCase.execute(dto, new AccountId(id)));
    }
}
