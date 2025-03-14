package com.arcathoria.character;

import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
class CharacterController {

    private final CharacterFacade characterFacade;

    CharacterController(final CharacterFacade characterFacade) {
        this.characterFacade = characterFacade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CharacterDTO create(@Valid @RequestBody CreateCharacterDTO dto) {
        return characterFacade.createCharacter(dto);
    }
}
