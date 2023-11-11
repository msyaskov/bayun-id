package dev.bayun.id.test;

import dev.bayun.id.security.oauth.configuration.OAuth2RegisteredClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(OAuth2RegisteredClientProperties.class)
class BayunSsoApplicationTests {

    @Autowired
    private OAuth2RegisteredClientProperties registeredClientProperties;

    @Test
    void contextLoads() {
        System.out.println(registeredClientProperties);
    }

}
