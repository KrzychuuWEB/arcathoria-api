package com.arcathoria.combat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.arcathoria.combat.CombatMapper.toDomain;
import static com.arcathoria.combat.CombatMapper.toEntityForInsert;

interface CombatJpaRepository extends JpaRepository<CombatEntity, UUID> {

}

@Repository
class CombatRepositoryAdapter implements CombatRepository {

    private final CombatJpaRepository repository;

    CombatRepositoryAdapter(final CombatJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Combat save(final Combat combat) {
        return toDomain(repository.save(
                toEntityForInsert(combat)
        ));
    }
}