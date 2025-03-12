package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterFacadeTest {

    @Mock
    private CreateCharacterUseCase createCharacterUseCase;

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
}