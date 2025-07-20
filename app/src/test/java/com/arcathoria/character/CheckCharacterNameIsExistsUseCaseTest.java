package com.arcathoria.character;

import com.arcathoria.character.exception.CharacterNameExistsException;
import com.arcathoria.character.vo.CharacterName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckCharacterNameIsExistsUseCaseTest {

    @Mock
    private CharacterQueryRepository characterQueryRepository;

    @InjectMocks
    private CheckCharacterNameIsExistsUseCase useCase;

    @Test
    void should_throw_exception_if_character_name_is_exists() {
        when(characterQueryRepository.existsByName(any(CharacterName.class))).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(new CharacterName("exampleName"))).isInstanceOf(CharacterNameExistsException.class);

        verify(characterQueryRepository).existsByName(any(CharacterName.class));
    }

    @Test
    void should_not_throw_exception_when_name_does_not_exist() {
        when(characterQueryRepository.existsByName(any(CharacterName.class))).thenReturn(false);

        useCase.execute(new CharacterName("exampleName"));

        verify(characterQueryRepository).existsByName(any(CharacterName.class));
    }
}