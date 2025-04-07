package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.command.CreateCharacterCommand;
import com.arcathoria.character.command.SelectCharacterCommand;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterFacadeTest {

    @Mock
    private CreateCharacterUseCase createCharacterUseCase;

    @Mock
    private SelectCharacterUseCase selectCharacterUseCase;

    @Mock
    private RemoveSelectedCharacterUseCase removeSelectedCharacterUseCase;

    @InjectMocks
    private CharacterFacade characterFacade;

    @Test
    void should_return_new_character_and_map_to_dto() {
        CreateCharacterDTO dto = new CreateCharacterDTO("exampleName");
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withCharacterId(UUID.randomUUID())
                        .withCharacterName(dto.characterName())
                        .withAccountId(accountId.value())
                        .build()
        );

        when(createCharacterUseCase.execute(any(CreateCharacterCommand.class))).thenReturn(character);

        CharacterDTO result = characterFacade.createCharacter(dto, accountId.value());

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.characterName()).isEqualTo(dto.characterName());

        verify(createCharacterUseCase).execute(any(CreateCharacterCommand.class));
    }

    @Test
    void should_return_select_character_when_valid_ids() {
        SelectCharacterCommand command = SelectCharacterCommandMother.aSelectCharacterCommand().build();
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withCharacterId(command.characterId().value())
                        .withAccountId(command.accountId().value())
                        .build()
        );

        when(selectCharacterUseCase.execute(any(SelectCharacterCommand.class))).thenReturn(character);

        CharacterDTO result = characterFacade.selectCharacter(new SelectCharacterDTO(command.characterId().value()), command.accountId().value());

        assertThat(result).isInstanceOf(CharacterDTO.class);
        assertThat(result.id()).isEqualTo(command.characterId().value());

        verify(selectCharacterUseCase).execute(any(SelectCharacterCommand.class));
    }

    @Test
    void should_remove_selected_character() {
        doNothing().when(removeSelectedCharacterUseCase).execute(any(AccountId.class));

        characterFacade.removeSelectedCharacter(UUID.randomUUID());

        verify(removeSelectedCharacterUseCase).execute(any(AccountId.class));
    }
}