package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CharacterPublicDTO;
import com.arcathoria.character.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterQueryFacadeTest {

    @Mock
    private GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase;

    @Mock
    private GetCharacterByIdUseCase getCharacterByIdUseCase;

    @Mock
    private GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase;

    @InjectMocks
    private CharacterQueryFacade characterQueryFacade;

    @Test
    void should_return_character_dto_list_when_valid_account_id_provided() {
        UUID accountId = UUID.randomUUID();
        List<Character> characters = sortedCharacterList(accountId);

        when(getAllCharactersByAccountIdUseCase.execute(any(AccountId.class))).thenReturn(characters);

        List<CharacterDTO> result = sortedCharacterDTOList(characterQueryFacade.getAllByAccountId(accountId));

        assertThat(result).hasSameSizeAs(characters);
        assertThat(result.get(0).characterName()).isEqualTo(characters.get(0).getSnapshot().getCharacterName().value());
        assertThat(result.get(1).characterName()).isEqualTo(characters.get(1).getSnapshot().getCharacterName().value());

        verify(getAllCharactersByAccountIdUseCase).execute(any(AccountId.class));
    }

    @Test
    void should_return_empty_list_when_characters_not_found() {
        when(getAllCharactersByAccountIdUseCase.execute(any(AccountId.class))).thenReturn(List.of());

        List<CharacterDTO> result = sortedCharacterDTOList(characterQueryFacade.getAllByAccountId(UUID.randomUUID()));

        assertThat(result).isEmpty();

        verify(getAllCharactersByAccountIdUseCase).execute(any(AccountId.class));
    }

    @Test
    void should_return_owned_character_when_valid_ids_are_provided() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenReturn(character);

        CharacterDTO result = characterQueryFacade.getOwnedCharacterById(characterId.value(), accountId.value());

        assertThat(result.id()).isEqualTo(characterId.value());
        assertThat(result.characterName()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);

        verify(getCharacterByIdUseCase).getOwned(any(CharacterId.class), any(AccountId.class));
    }

    @Test
    void should_return_selected_character_and_map_to_dto() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        AccountId accountId = new AccountId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(accountId.value())
                .build());

        when(getSelectedCharacterFromCacheUseCase.execute(any(AccountId.class))).thenReturn(character);

        CharacterDTO result = characterQueryFacade.getSelectedCharacter(accountId.value());

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(characterId.value());
        assertThat(result.characterName()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);

        verify(getSelectedCharacterFromCacheUseCase).execute(any(AccountId.class));
    }

    @Test
    void should_return_public_character_by_id_when_character_id_is_correct() {
        CharacterId characterId = new CharacterId(UUID.randomUUID());
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(characterId.value())
                .withAccountId(UUID.randomUUID())
                .build());

        when(getCharacterByIdUseCase.get(any(CharacterId.class))).thenReturn(character);

        CharacterPublicDTO result = characterQueryFacade.getPublicCharacterById(characterId.value());

        assertThat(result.id()).isEqualTo(characterId.value());
        assertThat(result.characterName()).isEqualTo(CharacterSnapshotMother.DEFAULT_CHARACTER_NAME);

        verify(getCharacterByIdUseCase).get(any(CharacterId.class));
    }

    private List<Character> sortedCharacterList(UUID accountId) {
        return Stream.of(
                        Character.restore(CharacterSnapshotMother.create().withAccountId(accountId).build()),
                        Character.restore(CharacterSnapshotMother.create().withCharacterName("exampleName2").withAccountId(accountId).build())
                )
                .sorted(Comparator.comparing(character -> character.getSnapshot().getCharacterName().value()))
                .toList();
    }

    private List<CharacterDTO> sortedCharacterDTOList(List<CharacterDTO> dtos) {
        return dtos.stream()
                .sorted(Comparator.comparing(CharacterDTO::characterName))
                .toList();
    }
}