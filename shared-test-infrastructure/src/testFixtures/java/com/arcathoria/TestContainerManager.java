package com.arcathoria;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerManager {

    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
    public static final RedisContainer REDIS_CONTAINER;

    private TestContainerManager() {
    }

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine")
                .withReuse(true);
        POSTGRES_CONTAINER.start();

        REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7-alpine"))
                .withReuse(true);
        REDIS_CONTAINER.start();
    }
}
