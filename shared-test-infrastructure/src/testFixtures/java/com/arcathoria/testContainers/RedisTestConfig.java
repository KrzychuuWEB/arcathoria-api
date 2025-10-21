package com.arcathoria.testContainers;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class RedisTestConfig {

    @Bean
    @ServiceConnection(name = "redis")
    RedisContainer redisContainer() {
        return TestContainerManager.redisContainer();
    }
}
