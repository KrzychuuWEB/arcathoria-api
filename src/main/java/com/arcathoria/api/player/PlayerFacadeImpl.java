package com.arcathoria.api.player;

import com.arcathoria.api.player.dto.CreatePlayerDTO;
import org.springframework.stereotype.Service;

@Service
class PlayerFacadeImpl implements PlayerFacade {

    private final PlayerFactory playerFactory;

    private final PlayerRepository playerRepository;

    PlayerFacadeImpl(final PlayerFactory playerFactory, final PlayerRepository playerRepository) {
        this.playerFactory = playerFactory;
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(final CreatePlayerDTO createPlayerDTO) {
        Player player = playerFactory.createPlayerFactory(createPlayerDTO);

        return playerRepository.save(player);
    }
}
