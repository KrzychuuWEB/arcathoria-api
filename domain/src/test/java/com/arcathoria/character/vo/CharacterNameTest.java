package com.arcathoria.character.vo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CharacterNameTest {

    @Test
    void should_create_character_name_when_valid_value_given() {
        String name = "Example_Character-Name2";
        CharacterName result = new CharacterName(name);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(name);
    }

    @Test
    void should_throw_exception_when_name_is_null() {
        assertThatThrownBy(() -> new CharacterName(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_name_is_empty() {
        assertThatThrownBy(() -> new CharacterName("")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_name_is_too_short() {
        assertThatThrownBy(() -> new CharacterName("12")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_name_is_too_long() {
        assertThatThrownBy(() -> new CharacterName("example_too_long_character-name")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_throw_exception_when_name_contains_special_characters() {
        assertThatThrownBy(() -> new CharacterName("special_characters_!@#")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void should_trim_whitespaces_before_validation() {
        CharacterName result = new CharacterName("    Example    ");

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo("Example");
    }
}