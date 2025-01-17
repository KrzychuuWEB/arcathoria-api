package com.arcathoria.api.player;

import com.arcathoria.api.player.dto.CreatePlayerDTO;
import com.arcathoria.api.player.dto.PlayerDTO;

public interface PlayerFacade {

    PlayerDTO createPlayer(CreatePlayerDTO player);
}
