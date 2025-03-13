package com.arcathoria.character;

import com.arcathoria.character.vo.CharacterName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

interface CharacterJpaQueryRepository extends JpaRepository<CharacterEntity, UUID> {

    boolean existsByName(String name);
}

@Repository
class CharacterQueryRepositoryAdapter implements CharacterQueryRepository {

    private final CharacterJpaQueryRepository repository;

    CharacterQueryRepositoryAdapter(final CharacterJpaQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByName(final CharacterName name) {
        return repository.existsByName(name.value());
    }
}