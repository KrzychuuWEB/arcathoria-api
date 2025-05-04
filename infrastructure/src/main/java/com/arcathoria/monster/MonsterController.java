package com.arcathoria.monster;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monsters")
class MonsterController {

    private final MonsterQueryFacade monsterQueryFacade;

    MonsterController(final MonsterQueryFacade monsterQueryFacade) {
        this.monsterQueryFacade = monsterQueryFacade;
    }

    
}
