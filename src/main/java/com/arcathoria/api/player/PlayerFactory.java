package com.arcathoria.api.player;

import com.arcathoria.api.player.dto.CreatePlayerDTO;
import org.springframework.stereotype.Component;

@Component
class PlayerFactory {

    Player createPlayerFactory(CreatePlayerDTO createPlayerDTO) {
        return Player.PlayerBuilder.aPlayer()
                .withUsername(createPlayerDTO.username())
                .withEmail(createPlayerDTO.email())
                .withPassword(createPlayerDTO.password())
                .build();
    }
}
