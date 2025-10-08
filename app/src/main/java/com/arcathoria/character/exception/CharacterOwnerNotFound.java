package com.arcathoria.character.exception;

import com.arcathoria.character.vo.AccountId;
import com.arcathoria.exception.UpstreamAware;
import com.arcathoria.exception.UpstreamInfo;

import java.util.Map;
import java.util.Optional;

public class CharacterOwnerNotFound extends CharacterApplicationException implements UpstreamAware {

    private final UpstreamInfo upstreamInfo;

    public CharacterOwnerNotFound(final AccountId accountId, final UpstreamInfo upstreamInfo) {
        super("Owner with id " + accountId.value() + " not found",
                CharacterExceptionErrorCode.ERR_CHARACTER_OWNER_NOT_FOUND,
                Map.of("accountId", accountId.value())
        );

        this.upstreamInfo = upstreamInfo;
    }

    @Override
    public Optional<UpstreamInfo> getUpstreamInfo() {
        return Optional.ofNullable(upstreamInfo);
    }
}
