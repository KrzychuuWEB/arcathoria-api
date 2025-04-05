package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
import com.arcathoria.character.vo.CharacterId;
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

        when(createCharacterUseCase.execute(any(CreateCharacterDTO.class), any(AccountId.class))).thenReturn(character);

        CharacterDTO result = characterFacade.createCharacter(dto, accountId.value());

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.characterName()).isEqualTo(dto.characterName());

        verify(createCharacterUseCase).execute(any(CreateCharacterDTO.class), any(AccountId.class));
    }

    @Test
    void should_return_select_character_when_valid_ids() {
        AccountId accountId = new AccountId(UUID.randomUUID());
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withCharacterId(characterId.value())
                        .withAccountId(accountId.value())
                        .build()
        );

        when(selectCharacterUseCase.execute(any(CharacterId.class), any(AccountId.class))).thenReturn(character);

        CharacterDTO result = characterFacade.selectCharacter(new SelectCharacterDTO(characterId.value()), accountId.value());

        assertThat(result).isInstanceOf(CharacterDTO.class);
        assertThat(result.id()).isEqualTo(characterId.value());

        verify(selectCharacterUseCase).execute(any(CharacterId.class), any(AccountId.class));
    }

    @Test
    void should_remove_selected_character() {
        doNothing().when(removeSelectedCharacterUseCase).execute(any(AccountId.class));

        characterFacade.removeSelectedCharacter(UUID.randomUUID());

        verify(removeSelectedCharacterUseCase).execute(any(AccountId.class));
    }
}