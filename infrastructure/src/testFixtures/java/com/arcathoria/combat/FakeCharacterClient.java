package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TestConfiguration
@Profile("test")
@Primary
class FakeCharacterClient implements CharacterClient {

    private final Map<AccountId, ParticipantView> selectedCharacters = new HashMap<>();
    private boolean shouldThrowException = false;
    private String exceptionType = null;

    @Override
    public ParticipantView getSelectedCharacterByAccountId(final AccountId accountId) {
        if (shouldThrowException) {
            throwConfiguredException(accountId);
        }

        ParticipantView participant = selectedCharacters.get(accountId);
        if (participant == null) {
            throw new CombatParticipantNotAvailableException(
                    new ParticipantId(accountId.value()),
                    new UpstreamInfo("character", "ERR_CHARACTER_NOT_SELECTED")
            );
        }
        return participant;
    }

    public FakeCharacterClient withCustomCharacter(final AccountId accountId, final ParticipantViewMother participantViewMother) {
        participantViewMother.withType(ParticipantType.PLAYER);
        selectedCharacters.put(accountId, participantViewMother.build());
        return this;
    }

    public FakeCharacterClient withDefaultCharacter(final AccountId accountId) {
        ParticipantView participant = ParticipantViewMother.aParticipant()
                .withName("character" + accountId.value())
                .withHealth(100)
                .withIntelligence(1)
                .build();
        selectedCharacters.put(accountId, participant);
        return this;
    }

    public FakeCharacterClient throwCharacterNotSelectedException() {
        this.shouldThrowException = true;
        this.exceptionType = "NOT_SELECTED";
        return this;
    }

    public FakeCharacterClient throwCharacterNotFoundException() {
        this.shouldThrowException = true;
        this.exceptionType = "NOT_FOUND";
        return this;
    }

    public FakeCharacterClient throwExternalServiceException() {
        this.shouldThrowException = true;
        this.exceptionType = "EXTERNAL_SERVICE";
        return this;
    }

    public void reset() {
        selectedCharacters.clear();
        shouldThrowException = false;
        exceptionType = null;
    }

    private void throwConfiguredException(final AccountId accountId) {
        switch (exceptionType) {
            case "NOT_SELECTED":
                throw new CombatParticipantNotAvailableException(
                        new ParticipantId(accountId.value()),
                        new UpstreamInfo("character", "ERR_CHARACTER_NOT_SELECTED")
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
}
