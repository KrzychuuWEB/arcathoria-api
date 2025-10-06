package com.arcathoria.combat;

import com.arcathoria.BaseFakeClient;
import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Primary
@Profile("test")
class FakeCharacterClient extends BaseFakeClient<UUID, ParticipantView> implements CharacterClient {
    
    @Override
    public ParticipantView getSelectedCharacterByAccountId(final AccountId accountId) {
        return get(accountId.value(), (id, errorCode) -> new CombatParticipantNotAvailableException(
                new ParticipantId(id),
                new UpstreamInfo("character", errorCode)
        ));
    }

    public FakeCharacterClient withCustomCharacter(final UUID accountId, final ParticipantViewMother participantViewMother) {
        participantViewMother.withType(ParticipantType.PLAYER);
        data.put(accountId, participantViewMother.build());
        return this;
    }

    public FakeCharacterClient withDefaultCharacter(final UUID accountId) {
        ParticipantView participant = ParticipantViewMother.aParticipant()
                .withName("character" + accountId)
                .withHealth(100)
                .withIntelligence(1)
                .build();
        data.put(accountId, participant);
        return this;
    }

    public FakeCharacterClient throwCharacterNotSelectedException() {
        configureException("NOT_SELECTED");
        return this;
    }

    public FakeCharacterClient throwCharacterNotFoundException() {
        configureException("NOT_FOUND");
        return this;
    }

    public FakeCharacterClient throwExternalServiceException() {
        configureException("EXTERNAL_SERVICE");
        return this;
    }

    @Override
    protected void throwConfiguredException(final UUID key) {
        switch (exceptionType) {
            case "NOT_SELECTED":
                throw new CombatParticipantNotAvailableException(
                        new ParticipantId(key),
                        new UpstreamInfo("character", getDefaultNotFoundErrorCode())
                );
            case "NOT_FOUND":
                throw new CombatParticipantNotAvailableException(
                        new ParticipantId(UUID.randomUUID()),
                        new UpstreamInfo("character", "ERR_CHARACTER_NOT_FOUND")
                );
            case "EXTERNAL_SERVICE":
                throw new ExternalServiceUnavailableException("character");
            default:
                throw new RuntimeException("Unknown exception type");
        }
    }

    @Override
    protected String getDefaultNotFoundErrorCode() {
        return "ERR_CHARACTER_NOT_SELECTED";
    }
}
