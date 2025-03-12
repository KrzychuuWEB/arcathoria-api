package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.exception.CharacterNameExistsException;
import com.arcathoria.character.vo.CharacterName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCharacterUseCaseTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CheckCharacterNameIsExistsUseCase checkCharacterNameIsExistsUseCase;

    @Mock
    private CharacterFactory characterFactory;

    @Mock
    private AccountQueryFacade accountQueryFacade;

    @InjectMocks
    private CreateCharacterUseCase createCharacterUseCase;

    @Test
    void should_create_new_character() {
        AccountDTO accountDTO = new AccountDTO(UUID.randomUUID(), "example@email.com");
        CreateCharacterDTO createCharacterDTO = new CreateCharacterDTO("exampleName");
        Character character = Character.restore(
                CharacterSnapshotMother.create()
                        .withAccountId(accountDTO.id())
                        .withCharacterName("exampleName")
                        .build()
        );

        when(accountQueryFacade.getById(any(UUID.class))).thenReturn(accountDTO);
        when(characterFactory.createCharacter(any(AccountId.class), any(CharacterName.class))).thenReturn(character);
        when(characterRepository.save(any(Character.class))).thenReturn(character);

        Character result = createCharacterUseCase.execute(createCharacterDTO, new AccountId(accountDTO.id()));

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getAccountId().value()).isEqualTo(accountDTO.id());
        assertThat(result.getSnapshot().getName().value()).isEqualTo(createCharacterDTO.characterName());

        verify(accountQueryFacade).getById(any(UUID.class));
        verify(checkCharacterNameIsExistsUseCase).execute(any(CharacterName.class));
        verify(characterFactory).createCharacter(any(AccountId.class), any(CharacterName.class));
        verify(characterRepository).save(any(Character.class));
    }

    @Test
    void should_propagate_exception_when_account_does_not_exist() {
        AccountDTO accountDTO = new AccountDTO(UUID.randomUUID(), "example@email.com");
        CreateCharacterDTO createCharacterDTO = new CreateCharacterDTO("exampleName");

        when(accountQueryFacade.getById(any(UUID.class))).thenThrow(AccountNotFoundException.class);

        assertThatThrownBy(() -> createCharacterUseCase.execute(createCharacterDTO, new AccountId(accountDTO.id())))
                .isInstanceOf(AccountNotFoundException.class);

        verify(accountQueryFacade).getById(any(UUID.class));
        verify(checkCharacterNameIsExistsUseCase, never()).execute(any(CharacterName.class));
        verify(characterFactory, never()).createCharacter(any(AccountId.class), any(CharacterName.class));
        verify(characterRepository, never()).save(any(Character.class));
    }

    @Test
    void should_throw_exception_when_character_name_already_exists() {
        AccountDTO accountDTO = new AccountDTO(UUID.randomUUID(), "example@email.com");
        CreateCharacterDTO createCharacterDTO = new CreateCharacterDTO("exampleName");

        when(accountQueryFacade.getById(any(UUID.class))).thenReturn(accountDTO);
        doThrow(new CharacterNameExistsException("exampleName")).when(checkCharacterNameIsExistsUseCase).execute(any(CharacterName.class));

        assertThatThrownBy(() -> createCharacterUseCase.execute(createCharacterDTO, new AccountId(accountDTO.id())))
                .isInstanceOf(CharacterNameExistsException.class);

        verify(accountQueryFacade).getById(any(UUID.class));
        verify(checkCharacterNameIsExistsUseCase).execute(any(CharacterName.class));
        verify(characterFactory, never()).createCharacter(any(AccountId.class), any(CharacterName.class));
        verify(characterRepository, never()).save(any(Character.class));
    }
}