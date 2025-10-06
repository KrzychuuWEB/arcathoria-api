package com.arcathoria.auth;

import com.arcathoria.testContainers.WithPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithPostgres
@Import({FakeAuthAccountClient.class})
class AuthenticateAccountTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private FakeAuthAccountClient fakeAccountClient;

    @Autowired
    private AuthenticateAccount authenticateAccount;

    @Test
    void should_generate_token_when_account_is_valid() {
        UUID accountId = UUID.randomUUID();
        String email = "arcathoria@arcathoria.com";
        String rawPassword = "password12345";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(email, rawPassword);

        fakeAccountClient.withAccount(email, new AccountView(accountId, email));
        String expectedToken = jwtTokenService.generateToken(email, accountId);

        String result = authenticateAccount.authenticate(authRequestDTO);

        assertThat(result).isEqualTo(expectedToken);
    }
}