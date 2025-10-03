package com.arcathoria.combat.exception;

import com.arcathoria.combat.vo.ParticipantId;
import com.arcathoria.exception.UpstreamAware;
import com.arcathoria.exception.UpstreamInfo;

import java.util.Map;
import java.util.Optional;

public class CombatParticipantNotAvailableException extends CombatApplicationException implements UpstreamAware {

    private final ParticipantId participantId;
    private final UpstreamInfo upstreamInfo;

    public CombatParticipantNotAvailableException(final ParticipantId participantId, final UpstreamInfo upstreamInfo) {
        super("Participant with id " + participantId.value() + " not found",
                CombatExceptionErrorCode.ERR_COMBAT_PARTICIPANT_NOT_AVAILABLE,
                Map.of("participantId", participantId.value())
        );
        this.participantId = participantId;
        this.upstreamInfo = upstreamInfo;
    }

    public ParticipantId ParticipantId() {
        return participantId;
    }

    @Override
    public Optional<UpstreamInfo> getUpstreamInfo() {
        return Optional.ofNullable(upstreamInfo);
    }
}
