package com.arcathoria.monster;

import com.arcathoria.WithPostgres;
import com.arcathoria.auth.AccountWithAuthenticated;
import com.arcathoria.auth.TestJwtTokenGenerator;
import com.arcathoria.monster.dto.MonsterDTO;
import com.arcathoria.monster.exception.MonsterExceptionErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithPostgres
@Import({TestJwtTokenGenerator.class})
class MonsterControllerModuleTest {

    private final String baseUrl = "/monsters";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestJwtTokenGenerator tokenGenerator;

    private AccountWithAuthenticated accountWithAuthenticated;

    @BeforeEach
    void setup() {
        this.accountWithAuthenticated = new AccountWithAuthenticated(tokenGenerator);
    }

    @Test
    void should_return_monster_by_id() {
        HttpHeaders headers = accountWithAuthenticated.authenticatedUser(UUID.randomUUID());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<MonsterDTO> response = restTemplate.exchange(
                baseUrl + "/bf4397d8-b4dc-361e-9b6d-191a352e9134",
                HttpMethod.GET,
                requestEntity,
                MonsterDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Wilk");
    }

    @Test
    void should_return_monster_not_found_exception_when_get_monster_by_id() {
        HttpHeaders headers = accountWithAuthenticated.authenticatedWithLangPL(UUID.randomUUID());
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                baseUrl + "/00000000-0000-0000-0000-000000000000",
                HttpMethod.GET,
                requestEntity,
                ProblemDetail.class
        );
        ProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result).isNotNull();
        assertThat(result.getDetail()).contains("potwora");
        assertThat(result.getProperties())
                .containsEntry("errorCode", MonsterExceptionErrorCode.ERR_MONSTER_NOT_FOUND.getCodeName());
    }
}