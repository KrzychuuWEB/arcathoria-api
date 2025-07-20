package com.arcathoria.character;

import com.arcathoria.account.vo.AccountId;
import com.arcathoria.character.vo.CharacterId;
import com.arcathoria.character.vo.CharacterName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CharacterJpaQueryRepository extends JpaRepository<CharacterEntity, UUID> {

    boolean existsByName(final String name);

    List<CharacterEntity> getAllByAccountId(final UUID accountId);
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

    @Override
    public List<Character> getAllByAccountId(final AccountId accountId) {
        return repository.getAllByAccountId(accountId.value())
                .stream()
                .map(CharacterMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Character> getById(final CharacterId characterId) {
        return repository.findById(characterId.value()).map(CharacterMapper::toDomain);
    }
}