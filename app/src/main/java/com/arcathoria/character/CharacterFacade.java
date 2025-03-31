package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import com.arcathoria.character.vo.CharacterId;

import java.util.UUID;

import static com.arcathoria.character.CharacterDTOMapper.toCharacterDTO;

public class CharacterFacade {

    private final CreateCharacterUseCase characterUseCase;
    private final SelectCharacterUseCase selectCharacterUseCase;

    CharacterFacade(final CreateCharacterUseCase characterUseCase,
                    final SelectCharacterUseCase selectCharacterUseCase
    ) {
        this.characterUseCase = characterUseCase;
        this.selectCharacterUseCase = selectCharacterUseCase;
    }

    public CharacterDTO createCharacter(final CreateCharacterDTO dto, final UUID accountId) {
        return toCharacterDTO(characterUseCase.execute(dto, new AccountId(accountId)));
    }

    CharacterDTO selectCharacter(final SelectCharacterDTO dto, final UUID accountId) {
        return toCharacterDTO(selectCharacterUseCase.execute(new CharacterId(dto.characterId()), new AccountId(accountId)));
    }
}
