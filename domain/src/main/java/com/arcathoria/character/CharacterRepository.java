package com.arcathoria.character;

interface CharacterRepository {

    Character save(Character character);

    boolean existsByEmail(String email);
}
