package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.dto.CharacterDTO;
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