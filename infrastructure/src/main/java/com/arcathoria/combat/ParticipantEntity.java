package com.arcathoria.combat;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "participants", uniqueConstraints = @UniqueConstraint(columnNames = {"combat_id", "character_id"}))
class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combat_id", nullable = false)
    private CombatEntity combat;

    private UUID characterId;

    @Enumerated(EnumType.STRING)
    private CombatSide side;

    private int currentHealth;

    private int maxHealth;

    private int intelligence;

    protected ParticipantEntity() {
    }

    ParticipantEntity(final UUID id, final CombatEntity combat, final UUID characterId,
                      final CombatSide side, final int currentHealth, final int maxHealth, final int intelligence
    ) {
        this.id = id;
        this.combat = combat;
        this.characterId = characterId;
        this.side = side;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.intelligence = intelligence;
    }

    UUID getId() {
        return id;
    }

    CombatEntity getCombat() {
        return combat;
    }

    UUID getCharacterId() {
        return characterId;
    }

    CombatSide getSide() {
        return side;
    }

    int getCurrentHealth() {
        return currentHealth;
    }

    int getMaxHealth() {
        return maxHealth;
    }

    int getIntelligence() {
        return intelligence;
    }

    void setCombat(final CombatEntity combat) {
        this.combat = combat;
    }
}
