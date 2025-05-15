package com.arcathoria.monster;

import com.arcathoria.ApiErrorResponse;
import com.arcathoria.IntegrationTestContainersConfig;
import com.arcathoria.SetLocaleHelper;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.monster.dto.MonsterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonsterControllerE2ETest extends IntegrationTestContainersConfig {

    private final String baseUrl = "/monsters";
    @Autowired
    private TestRestTemplate restTemplate;
    private AccountManagerE2EHelper accountManagerE2EHelper;

    @BeforeEach
    void setup() {
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
    }

    @Test
    void should_return_monster_by_id() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("getmonsterbyid@email.com");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<MonsterDTO> response = restTemplate.exchange(
                baseUrl + "/wolf",
                HttpMethod.GET,
                requestEntity,
                MonsterDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo("wolf");
    }

    @Test
    void should_return_monster_not_found_exception_when_get_monster_by_id() {
        HttpHeaders headers = accountManagerE2EHelper.registerAndGetAuthHeaders("notfoundmonsterbyid@email.com");
        SetLocaleHelper.withLocale(headers, "pl");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(
                baseUrl + "/badmonsterid",
                HttpMethod.GET,
                requestEntity,
                ApiErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrorCode()).isEqualTo("ERR_MONSTER_NOT_FOUND-404");
        assertThat(response.getBody().getMessage()).contains("potwora");
    }
}