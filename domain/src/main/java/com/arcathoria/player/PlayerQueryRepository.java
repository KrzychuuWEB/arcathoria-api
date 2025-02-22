package com.arcathoria.player;

import java.util.Optional;

interface PlayerQueryRepository {
    Optional<Player> findByUsername(String username);
}
