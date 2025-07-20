package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.command.CreateCharacterCommand;
import com.arcathoria.character.command.SelectCharacterCommand;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;

import java.util.UUID;

import static com.arcathoria.character.CharacterDTOMapper.toCharacterDTO;

public class CharacterFacade {

    private final CreateCharacterUseCase characterUseCase;
    private final SelectCharacterUseCase selectCharacterUseCase;
    private final RemoveSelectedCharacterUseCase removeSelectedCharacterUseCase;

    CharacterFacade(final CreateCharacterUseCase characterUseCase,
                    final SelectCharacterUseCase selectCharacterUseCase,
                    final RemoveSelectedCharacterUseCase removeSelectedCharacterUseCase
    ) {
        this.characterUseCase = characterUseCase;
        this.selectCharacterUseCase = selectCharacterUseCase;
        this.removeSelectedCharacterUseCase = removeSelectedCharacterUseCase;
    }

    public CharacterDTO createCharacter(final CreateCharacterDTO dto, final UUID accountId) {
        CreateCharacterCommand command = new CreateCharacterCommand(
                new AccountId(accountId),
                new CharacterName(dto.characterName())
        );

        return toCharacterDTO(characterUseCase.execute(command));
    }

    public CharacterDTO selectCharacter(final SelectCharacterDTO dto, final UUID accountId) {
        SelectCharacterCommand command = new SelectCharacterCommand(
                new AccountId(accountId),
                new CharacterId(dto.characterId())
        );
        return toCharacterDTO(selectCharacterUseCase.execute(command));
    }

    public void removeSelectedCharacter(final UUID accountId) {
        removeSelectedCharacterUseCase.execute(new AccountId(accountId));
    }
}
