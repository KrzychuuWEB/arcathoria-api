package com.arcathoria.character;

import com.arcathoria.auth.AccountPrincipal;
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
    List<CharacterDTO> getAllByAccountId(@AuthenticationPrincipal AccountPrincipal principal) {
        return characterQueryFacade.getAllByAccountId(principal.id());
    }

    @PostMapping("/selects")
    @ResponseStatus(HttpStatus.OK)
    CharacterDTO selectCharacter(
            @Valid @RequestBody SelectCharacterDTO dto,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return characterFacade.selectCharacter(dto, principal.id());
    }

    @GetMapping("/selects/me")
    @ResponseStatus(HttpStatus.OK)
    CharacterDTO getSelectedCharacter(@AuthenticationPrincipal AccountPrincipal principal) {
        return characterQueryFacade.getSelectedCharacter(principal.id());
    }

    @DeleteMapping("/selects")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeSelectedCharacter(@AuthenticationPrincipal AccountPrincipal principal) {
        characterFacade.removeSelectedCharacter(principal.id());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CharacterDTO create(
            @Valid @RequestBody CreateCharacterDTO dto,
            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return createCharacterTransactionalAdapter.transactionalCreateCharacter(dto, principal.id());
    }
}
