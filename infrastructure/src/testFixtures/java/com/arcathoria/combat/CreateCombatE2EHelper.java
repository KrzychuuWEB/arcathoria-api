package com.arcathoria.combat;


import com.arcathoria.combat.dto.CombatResultDTO;
import com.arcathoria.combat.dto.InitPveDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

class CreateCombatE2EHelper {

    private final String combatInitUrl = "/combats/init";

    private TestRestTemplate restTemplate;

    CreateCombatE2EHelper(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseEntity<CombatResultDTO> initPveCombat(final InitPveDTO initPveDTO, final HttpHeaders headers) {
        return restTemplate.postForEntity(combatInitUrl + "/pve", new HttpEntity<>(initPveDTO, headers), CombatResultDTO.class);
    }
}
