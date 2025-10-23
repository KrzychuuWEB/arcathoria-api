package com.arcathoria.auth;

import com.arcathoria.ApiProblemDetail;
import com.arcathoria.testContainers.WithPostgres;
import org.junit.jupiter.api.AfterEach;
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
@Import(FakeJwtTokenConfig.class)
class AuthControllerModuleTest {

    private String authenticateUrl = "/authenticate";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FakeAuthAccountClient fakeAuthAccountClient;

    @Autowired
    private AccountWithAuthenticated accountWithAuthenticated;

    @AfterEach
    void tearDown() {
        fakeAuthAccountClient.reset();
    }

    @Test
    void should_authenticate_success_and_set_session_cookie() {
        String email = "email@arcathori.com";
        String rawPassword = "rawPassword123";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(email, rawPassword);

        fakeAuthAccountClient.withAccount(email, new AccountView(UUID.randomUUID(), rawPassword));
        
        ResponseEntity<Void> response = restTemplate.postForEntity(authenticateUrl, new HttpEntity<>(authRequestDTO), Void.class);

        String sessionCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sessionCookie).isNotNull();
        assertThat(sessionCookie).contains("session=");
        assertThat(sessionCookie).containsIgnoringCase("HttpOnly");
    }

    @Test
    void should_invalid_credentials_return_unauthorized() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("invalid@email.com", "secret_invalid_password123");

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(authenticateUrl, new HttpEntity<>(authRequestDTO), ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void should_return_auth_bad_credentials_exception_when_account_not_authorized_by_credentials() {
        String email = "email@arcathori.com";
        String rawPassword = "rawPassword123";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(email, rawPassword);

        fakeAuthAccountClient.withAccount(email, new AccountView(UUID.randomUUID(), rawPassword)).throwBadCredentialsException();

        ResponseEntity<ApiProblemDetail> response = restTemplate.postForEntity(authenticateUrl, new HttpEntity<>(authRequestDTO), ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("ERR AUTH BAD CREDENTIALS");
        assertThat(result.getDetail()).isEqualTo("Bad credentials");
        assertThat(result.getErrorCode()).isEqualTo(AuthExceptionErrorCode.ERR_AUTH_BAD_CREDENTIALS.getCodeName());
    }

    @Test
    void should_return_external_exception_when_account_throw_other_exceptions() {
        String email = "email@arcathori.com";
        String rawPassword = "rawPassword123";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(email, rawPassword);

        fakeAuthAccountClient.withAccount(email, new AccountView(UUID.randomUUID(), rawPassword)).throwExternalServiceException();

        ResponseEntity<ApiProblemDetail> response = restTemplate.postForEntity(authenticateUrl, new HttpEntity<>(authRequestDTO), ApiProblemDetail.class);
        ApiProblemDetail result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(result.getDetail()).isEqualTo("Internal server error.");
    }
}