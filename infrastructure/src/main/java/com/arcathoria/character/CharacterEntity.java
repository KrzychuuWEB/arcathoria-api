package com.arcathoria.character;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "characters")
class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    private String name;

    private Double maxHealth;

    private Integer intelligence;

    protected CharacterEntity() {
    }

    CharacterEntity(final UUID id, final UUID accountId, final String name, final Double maxHealth, final Integer intelligence) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.maxHealth = maxHealth;
        this.intelligence = intelligence;
    }

    UUID getId() {
        return id;
    }

    UUID getAccountId() {
        return accountId;
    }

    String getName() {
        return name;
    }

    Double getMaxHealth() {
        return maxHealth;
    }

    Integer getIntelligence() {
        return intelligence;
    }
}
