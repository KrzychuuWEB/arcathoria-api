package com.arcathoria.character;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.arcathoria.character.CharacterMapper.toDomain;
import static com.arcathoria.character.CharacterMapper.toEntity;

interface CharacterJpaRepository extends JpaRepository<CharacterEntity, UUID> {

}

@Repository
class CharacterRepositoryAdapter implements CharacterRepository {

    private final CharacterJpaRepository repository;

    CharacterRepositoryAdapter(final CharacterJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Character save(final Character character) {
        return toDomain(repository.save(
                toEntity(character)
        ));
    }
}
