package com.arcathoria.combat;

import com.arcathoria.BaseFakeClient;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.MonsterId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Primary
@Profile("test")
class FakeMonsterClient extends BaseFakeClient<UUID, ParticipantView> implements MonsterClient {

    @Override
    public ParticipantView getMonsterById(final MonsterId monsterId) {
        return get(monsterId.value(), (id, errorCode) -> new CombatParticipantNotAvailableException(
                new ParticipantId(id),
                new UpstreamInfo("monster", errorCode)
        ));
    }

    public FakeMonsterClient withCustomMonster(final UUID monsterId, final ParticipantViewMother participantViewMother) {
        participantViewMother.withId(monsterId);
        participantViewMother.withType(ParticipantType.MONSTER);
        data.put(monsterId, participantViewMother.build());
        return this;
    }

    public FakeMonsterClient withDefaultMonster(final UUID monsterId) {
        ParticipantView participant = ParticipantViewMother.aParticipant()
                .withId(monsterId)
                .withName("wolf")
                .withHealth(100)
                .withIntelligence(1)
                .withType(ParticipantType.MONSTER)
                .build();
        data.put(monsterId, participant);
        return this;
    }

    public FakeMonsterClient withNotFoundException() {
        configureException("NOT_FOUND");
        return this;
    }

    public FakeMonsterClient throwExternalServiceException() {
        configureException("EXTERNAL_SERVICE");
        return this;
    }

    @Override
    protected void throwConfiguredException(final UUID key) {
        switch (exceptionType) {
            case "NOT_FOUND":
                throw new CombatParticipantNotAvailableException(
                        new ParticipantId(key),
                        new UpstreamInfo("monster", getDefaultNotFoundErrorCode())
                );
            case "EXTERNAL_SERVICE":
                throw new ExternalServiceUnavailableException("monster");
            default:
                throw new RuntimeException("Unknown exception type");
        }
    }

    @Override
    protected String getDefaultNotFoundErrorCode() {
        return "ERR_MONSTER_NOT_FOUND";
    }
}
