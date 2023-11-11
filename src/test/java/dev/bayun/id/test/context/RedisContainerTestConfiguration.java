package dev.bayun.id.test.context;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Максим Яськов
 */
@TestConfiguration
public class RedisContainerTestConfiguration {

    private static final GenericContainer<?> redisContainer;
    static {
        redisContainer = new GenericContainer<>(DockerImageName.parse("bitnami/redis:latest"))
                .withEnv("REDIS_PASSWORD", "pass")
                .withExposedPorts(6379);
        System.out.println("START REDIS SERVER");
        redisContainer.start();
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
    }

    @PostConstruct
    public void start() {
//        System.out.println("START REDIS SERVER");
//        redisContainer.start();
    }

    @PreDestroy
    public void stop() {
//        System.out.println("STOP REDIS SERVER");
//        redisContainer.stop();
    }



}
