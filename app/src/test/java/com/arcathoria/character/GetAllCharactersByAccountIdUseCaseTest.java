package com.arcathoria.character;

import com.arcathoria.account.AccountQueryFacade;
import com.arcathoria.account.dto.AccountDTO;
import com.arcathoria.account.exception.AccountNotFoundException;
import com.arcathoria.account.vo.AccountId;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCharactersByAccountIdUseCaseTest {

    @Mock
    private CharacterQueryRepository characterQueryRepository;

    @Mock
    private AccountQueryFacade accountQueryFacade;

    @InjectMocks
    private GetAllCharactersByAccountIdUseCase getAllCharactersByAccountIdUseCase;

    @Test
    void should_return_character_list_when_account_exists() {
        AccountDTO accountDTO = getAccountDto();

        List<Character> characters = sortedCharacterList(accountDTO.id());

        when(accountQueryFacade.getById(any(UUID.class))).thenReturn(accountDTO);
        when(characterQueryRepository.getAllByAccountId(any(AccountId.class))).thenReturn(characters);

        List<Character> result = getAllCharactersByAccountIdUseCase.execute(new AccountId(accountDTO.id()));

        assertThat(result).hasSameSizeAs(characters);
        assertThat(result.get(0).getSnapshot().getCharacterName()).isEqualTo(characters.get(0).getSnapshot().getCharacterName());
        assertThat(result.get(1).getSnapshot().getCharacterName()).isEqualTo(characters.get(1).getSnapshot().getCharacterName());

        verify(accountQueryFacade).getById(any(UUID.class));
        verify(characterQueryRepository).getAllByAccountId(any(AccountId.class));
    }

    @Test
    void should_return_empty_list_when_account_exists() {
        AccountDTO accountDTO = getAccountDto();

        when(accountQueryFacade.getById(any(UUID.class))).thenReturn(accountDTO);
        when(characterQueryRepository.getAllByAccountId(any(AccountId.class))).thenReturn(List.of());

        List<Character> result = getAllCharactersByAccountIdUseCase.execute(new AccountId(accountDTO.id()));

        assertThat(result).isEmpty();
    }

    @Test
    void should_throw_AccountNotFoundException_when_account_does_not_exist() {
        AccountDTO accountDTO = getAccountDto();

        when(accountQueryFacade.getById(any(UUID.class))).thenThrow(AccountNotFoundException.class);

        assertThatThrownBy(() -> getAllCharactersByAccountIdUseCase.execute(new AccountId(accountDTO.id()))).isInstanceOf(AccountNotFoundException.class);

        verify(accountQueryFacade).getById(any(UUID.class));
    }

    private AccountDTO getAccountDto() {
        return new AccountDTO(UUID.randomUUID(), "email@email.com");
    }

    private List<Character> sortedCharacterList(UUID accountId) {
        return Stream.of(
                        Character.restore(CharacterSnapshotMother.create().withAccountId(accountId).build()),
                        Character.restore(CharacterSnapshotMother.create().withCharacterName("exampleName2").withAccountId(accountId).build())
                )
                .sorted(Comparator.comparing(character -> character.getSnapshot().getCharacterName().value()))
                .toList();
    }
}