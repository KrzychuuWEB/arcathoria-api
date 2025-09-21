package com.arcathoria.combat;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "combats")
class CombatEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CombatType type;

    @Enumerated(EnumType.STRING)
    private CombatSide side;

    @Enumerated(EnumType.STRING)
    private CombatStatus status;

    @OneToMany(mappedBy = "combat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ParticipantEntity> participants = new ArrayList<>();

    protected CombatEntity() {
    }

    CombatEntity(final UUID id, final CombatType type, final CombatSide side, final CombatStatus status) {
        this.id = id;
        this.type = type;
        this.side = side;
        this.status = status;
    }

    UUID getId() {
        return id;
    }

    CombatType getType() {
        return type;
    }

    CombatSide getSide() {
        return side;
    }

    CombatStatus getStatus() {
        return status;
    }

    public void addParticipant(final ParticipantEntity participant) {
        participants.add(participant);
        participant.setCombat(this);
    }

    List<ParticipantEntity> getParticipants() {
        return participants;
    }
}