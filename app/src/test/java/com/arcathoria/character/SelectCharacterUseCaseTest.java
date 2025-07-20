package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.command.SelectCharacterCommand;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.exception.AccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectCharacterUseCaseTest {

    @Mock
    private GetCharacterByIdUseCase getCharacterByIdUseCase;

    @Mock
    private SelectCharacterCachePort selectCharacterCachePort;

    @InjectMocks
    private SelectCharacterUseCase selectCharacterUseCase;

    @Test
    void should_return_character_when_account_owned_character() {
        SelectCharacterCommand command = SelectCharacterCommandMother.aSelectCharacterCommand().build();
        Character character = Character.restore(CharacterSnapshotMother.create()
                .withCharacterId(command.characterId().value())
                .withAccountId(command.accountId().value())
                .build());

        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenReturn(character);

        Character result = selectCharacterUseCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result.getSnapshot().getCharacterId()).isEqualTo(command.characterId());
        assertThat(result.getSnapshot().getAccountId()).isEqualTo(command.accountId());

        verify(getCharacterByIdUseCase).getOwned(any(CharacterId.class), any(AccountId.class));
        verify(selectCharacterCachePort).setValueAndSetExpiredTime(any(CharacterId.class), any(AccountId.class));
    }

    @Test
    void should_return_CharacterNotFoundException_when_character_not_found() {
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenThrow(CharacterNotFoundException.class);

        assertThatThrownBy(() -> selectCharacterUseCase.execute(
                SelectCharacterCommandMother.aSelectCharacterCommand().build()
        )).isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    void should_return_AccessDeniedException_when_account_not_owned_character() {
        when(getCharacterByIdUseCase.getOwned(any(CharacterId.class), any(AccountId.class))).thenThrow(AccessDeniedException.class);

        assertThatThrownBy(() -> selectCharacterUseCase.execute(
                SelectCharacterCommandMother.aSelectCharacterCommand().build()
        )).isInstanceOf(AccessDeniedException.class);
    }
}