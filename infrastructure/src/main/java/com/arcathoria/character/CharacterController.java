package com.arcathoria.character;

import com.arcathoria.account.MyUserDetails;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
class CharacterController {

    private final CharacterFacade characterFacade;
    private final CharacterQueryFacade characterQueryFacade;

    CharacterController(
            final CharacterFacade characterFacade,
            final CharacterQueryFacade characterQueryFacade
    ) {
        this.characterFacade = characterFacade;
        this.characterQueryFacade = characterQueryFacade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CharacterDTO create(
            @Valid @RequestBody CreateCharacterDTO dto,
            @AuthenticationPrincipal MyUserDetails details
    ) {
        return characterFacade.createCharacter(dto, details.getId());
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CharacterDTO> getAllByAccountId(@AuthenticationPrincipal MyUserDetails details) {
        return characterQueryFacade.getAllByAccountId(details.getId());
    }
}
