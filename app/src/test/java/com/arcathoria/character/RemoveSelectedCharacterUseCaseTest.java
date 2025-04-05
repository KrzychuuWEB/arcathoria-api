package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.exception.CharacterNotFoundException;
import com.arcathoria.exception.AccessDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveSelectedCharacterUseCaseTest {

    @Mock
    private SelectCharacterCachePort selectCharacterCachePort;

    @Mock
    private GetSelectedCharacterFromCacheUseCase getSelectedCharacterFromCacheUseCase;

    @InjectMocks
    private RemoveSelectedCharacterUseCase removeSelectedCharacterUseCase;

    @Test
    void should_remove_selected_character_from_cache() {
        Character character = Character.restore(CharacterSnapshotMother.create().build());

        when(getSelectedCharacterFromCacheUseCase.execute(any(AccountId.class))).thenReturn(character);

        removeSelectedCharacterUseCase.execute(new AccountId(UUID.randomUUID()));

        verify(getSelectedCharacterFromCacheUseCase).execute(any(AccountId.class));
        verify(selectCharacterCachePort).remove(any(AccountId.class));
    }

    @Test
    void should_remove_selected_character_return_CharacterNotFoundException() {
        when(getSelectedCharacterFromCacheUseCase.execute(any(AccountId.class))).thenThrow(CharacterNotFoundException.class);

        assertThatThrownBy(() -> removeSelectedCharacterUseCase.execute(new AccountId(UUID.randomUUID()))).isInstanceOf(CharacterNotFoundException.class);

        verify(getSelectedCharacterFromCacheUseCase).execute(any(AccountId.class));
        verify(selectCharacterCachePort, never()).remove(any(AccountId.class));
    }

    @Test
    void should_remove_selected_character_return_AccessDeniedException() {
        when(getSelectedCharacterFromCacheUseCase.execute(any(AccountId.class))).thenThrow(AccessDeniedException.class);

        assertThatThrownBy(() -> removeSelectedCharacterUseCase.execute(new AccountId(UUID.randomUUID()))).isInstanceOf(AccessDeniedException.class);

        verify(getSelectedCharacterFromCacheUseCase).execute(any(AccountId.class));
        verify(selectCharacterCachePort, never()).remove(any(AccountId.class));
    }
}