package com.arcathoria.combat;

import com.arcathoria.account.MyUserDetails;
import com.arcathoria.combat.dto.CombatIdDTO;
import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.ExecuteActionDTO;
import com.arcathoria.combat.dto.InitPveDTO;
import com.arcathoria.combat.exception.CombatNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/combats")
class CombatController {

    private final CombatFacade combatFacade;
    private final CombatQueryFacade combatQueryFacade;

    CombatController(final CombatFacade combatFacade, final CombatQueryFacade combatQueryFacade) {
        this.combatFacade = combatFacade;
        this.combatQueryFacade = combatQueryFacade;
    }

    @PostMapping("/init/pve")
    @ResponseStatus(HttpStatus.CREATED)
    CombatResultDTO initPveCombat(
            @Valid @RequestBody InitPveDTO dto,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        return combatFacade.initPVECombat(userDetails.getId(), dto);
    }

    @PostMapping("/{id}/actions/execute")
    @ResponseStatus(HttpStatus.OK)
    CombatResultDTO performActionInCombat(
            @PathVariable final UUID id,
            @Valid @RequestBody ExecuteActionDTO dto,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        if (!id.equals(dto.combatId())) {
            throw new CombatNotFoundException(id);
        }

        return combatFacade.performActionInCombat(userDetails.getId(), dto);
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    CombatIdDTO getActiveCombatByParticipantId(@AuthenticationPrincipal MyUserDetails userDetails) {
        return new CombatIdDTO(combatQueryFacade.getActiveCombatForSelectedCharacterByAccountId(userDetails.getId()));
    }
}
