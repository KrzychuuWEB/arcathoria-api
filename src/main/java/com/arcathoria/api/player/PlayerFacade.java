package com.arcathoria.api.player;

import com.arcathoria.api.player.dto.CreatePlayerDTO;

public interface PlayerFacade {

    Player createPlayer(CreatePlayerDTO createPlayerDTO);
}
