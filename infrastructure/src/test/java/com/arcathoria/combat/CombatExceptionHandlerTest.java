package com.arcathoria.combat;

import com.arcathoria.IntegrationTestContainersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest
class CombatExceptionHandlerTest extends IntegrationTestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

     
}