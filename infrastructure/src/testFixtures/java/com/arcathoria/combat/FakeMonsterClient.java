package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@Profile("test")
@Primary
class FakeMonsterClient implements MonsterClient {

    private final Map<MonsterId, ParticipantView> monsters = new HashMap<>();
    private boolean shouldThrowException = false;
    private String exceptionType = null;

    @Override
    public ParticipantView getMonsterById(final MonsterId monsterId) {
        if (shouldThrowException) {
            throwConfiguredException(monsterId);
        }

        ParticipantView participant = monsters.get(monsterId);
        if (participant == null) {
            throw new CombatParticipantNotAvailableException(
                    new ParticipantId(monsterId.value()),
                    new UpstreamInfo("monster", "ERR_MONSTER_NOT_FOUND")
            );
        }
        return participant;
    }

    public FakeMonsterClient withCustomMonster(final MonsterId monsterId, final ParticipantViewMother participantViewMother) {
        participantViewMother.withId(monsterId.value());
        participantViewMother.withType(ParticipantType.MONSTER);
        monsters.put(monsterId, participantViewMother.build());
        return this;
    }

    public FakeMonsterClient withDefaultMonster(final MonsterId monsterId) {
        ParticipantView participant = ParticipantViewMother.aParticipant()
                .withId(monsterId.value())
                .withName("wolf")
                .withHealth(100)
                .withIntelligence(1)
                .withType(ParticipantType.MONSTER)
                .build();
        monsters.put(monsterId, participant);
        return this;
    }

    public FakeMonsterClient withNotFoundException() {
        shouldThrowException = true;
        exceptionType = "NOT_FOUND";
        return this;
    }

    public FakeMonsterClient throwExternalServiceException() {
        this.shouldThrowException = true;
        this.exceptionType = "EXTERNAL_SERVICE";
        return this;
    }

    public void reset() {
        monsters.clear();
        shouldThrowException = false;
        exceptionType = null;
    }

    private void throwConfiguredException(final MonsterId monsterId) {
        switch (exceptionType) {
            case "NOT_FOUND":
                throw new CombatParticipantNotAvailableException(
                        new ParticipantId(monsterId.value()),
                        new UpstreamInfo("monster", "ERR_MONSTER_NOT_FOUND")
                );
            case "EXTERNAL_SERVICE":
                throw new ExternalServiceUnavailableException("monster");
            default:
                throw new RuntimeException("Unknown exception type");
        }
    }
}
