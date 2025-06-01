package com.arcathoria.monster.vo;

import com.arcathoria.character.vo.Health;

import java.util.UUID;

public record Participant(UUID id, Health health) {
}
