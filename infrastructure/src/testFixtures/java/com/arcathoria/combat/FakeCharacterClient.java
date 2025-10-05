package com.arcathoria.combat;

import com.arcathoria.combat.dto.ParticipantView;
import com.arcathoria.combat.exception.CombatParticipantNotAvailableException;
import com.arcathoria.combat.exception.ExternalServiceUnavailableException;
import com.arcathoria.combat.vo.AccountId;
import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public FakeCharacterClient withCustomCharacter(final AccountId accountId, final ParticipantView participant) {
        selectedCharacters.put(accountId, participant);
        return this;
    }

    public FakeCharacterClient withDefaultCharacter(final AccountId accountId) {
        UUID characterId = UUID.randomUUID();
        ParticipantView participant = new ParticipantView(
                characterId,
                "Test Warrior",
                100,
                50,
                ParticipantType.PLAYER
        );
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

    public void clear() {
        reset();
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
