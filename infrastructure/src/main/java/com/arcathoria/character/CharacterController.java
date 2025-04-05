package com.arcathoria.character;

import com.arcathoria.account.MyUserDetails;
import com.arcathoria.character.dto.CharacterDTO;
import com.arcathoria.character.dto.CreateCharacterDTO;
import com.arcathoria.character.dto.SelectCharacterDTO;
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
    private final CreateCharacterTransactionalAdapter createCharacterTransactionalAdapter;

    CharacterController(
            final CharacterFacade characterFacade,
            final CharacterQueryFacade characterQueryFacade,
            final CreateCharacterTransactionalAdapter createCharacterTransactionalAdapter
    ) {
        this.characterFacade = characterFacade;
        this.characterQueryFacade = characterQueryFacade;
        this.createCharacterTransactionalAdapter = createCharacterTransactionalAdapter;
    }

    @GetMapping("/selects")
    @ResponseStatus(HttpStatus.OK)
    List<CharacterDTO> getAllByAccountId(@AuthenticationPrincipal MyUserDetails details) {
        return characterQueryFacade.getAllByAccountId(details.getId());
    }

    @PostMapping("/selects")
    @ResponseStatus(HttpStatus.OK)
    CharacterDTO selectCharacter(
            @Valid @RequestBody SelectCharacterDTO dto,
            @AuthenticationPrincipal MyUserDetails details
    ) {
        return characterFacade.selectCharacter(dto, details.getId());
    }

    @GetMapping("/selects/me")
    @ResponseStatus(HttpStatus.OK)
    CharacterDTO getSelectedCharacter(@AuthenticationPrincipal MyUserDetails details) {
        return characterQueryFacade.getSelectedCharacter(details.getId());
    }

    @DeleteMapping("/selects")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeSelectedCharacter(@AuthenticationPrincipal MyUserDetails details) {
        characterFacade.removeSelectedCharacter(details.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CharacterDTO create(
            @Valid @RequestBody CreateCharacterDTO dto,
            @AuthenticationPrincipal MyUserDetails details
    ) {
        return createCharacterTransactionalAdapter.transactionalCreateCharacter(dto, details.getId());
    }
}
