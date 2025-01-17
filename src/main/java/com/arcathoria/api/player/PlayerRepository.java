package com.arcathoria.api.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PlayerRepository extends JpaRepository<Player, Long> {
}
