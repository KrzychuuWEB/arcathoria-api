package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;

import java.util.UUID;

public class CharacterFacade {

    private final CreateCharacterUseCase characterUseCase;

    CharacterFacade(final CreateCharacterUseCase characterUseCase) {
        this.characterUseCase = characterUseCase;
    }

    public CharacterDTO createCharacter(final CreateCharacterDTO dto, final UUID id) {
        return toDto(characterUseCase.execute(dto, new AccountId(id)));
    }

    private CharacterDTO toDto(Character character) {
        CharacterSnapshot snapshot = character.getSnapshot();
        return new CharacterDTO(
                snapshot.getCharacterId().value(),
                snapshot.getCharacterName().value()
        );
    }
}
