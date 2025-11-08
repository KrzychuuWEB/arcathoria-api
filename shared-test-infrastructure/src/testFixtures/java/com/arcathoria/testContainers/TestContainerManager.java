package com.arcathoria.testContainers;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerManager {

    private static final String POSTGRES_IMAGE = "postgres:16-alpine";
    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7-alpine");

    private TestContainerManager() {
    }

    public static PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(POSTGRES_IMAGE)
                .withReuse(true);
    }

    public static RedisContainer redisContainer() {
        return new RedisContainer(REDIS_IMAGE)
                .withReuse(true);
    }
}
