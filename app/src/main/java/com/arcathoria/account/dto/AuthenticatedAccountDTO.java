package com.arcathoria.account.dto;

import java.util.UUID;

public record AuthenticatedAccountDTO(UUID accountId, String email) {
}
