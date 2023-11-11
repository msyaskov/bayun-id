package dev.bayun.id.security.oauth.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Максим Яськов
 */
@Data
@ConfigurationProperties("oauth2")
public class OAuth2RegisteredClientProperties {

    private Map<String, Client> client;

    @Data
    public static class Client {

        private String clientId;

        private String clientSecret;

        private String clientAuthenticationMethod;

        private List<String> authorizationGrantType;

        private List<String> redirectUri;

        private Set<String> scope;

        private Token token;

    }

    @Data
    public static class Token {

        @DurationUnit(ChronoUnit.SECONDS)
        private Duration accessTokenLifetime = Duration.of(30, ChronoUnit.MINUTES);

        private String accessTokenFormat = OAuth2TokenFormat.SELF_CONTAINED.getValue();

        @DurationUnit(ChronoUnit.SECONDS)
        private Duration authorizationCodeLifetime = Duration.of(30, ChronoUnit.SECONDS);

        @DurationUnit(ChronoUnit.SECONDS)
        private Duration refreshTokenLifetime = Duration.of(1, ChronoUnit.DAYS);

        private boolean refreshTokenReuse = false;

        @DurationUnit(ChronoUnit.SECONDS)
        private Duration deviceCodeLifetime = Duration.of(30, ChronoUnit.MINUTES);

        private SignatureAlgorithm idTokenAlgorithm = SignatureAlgorithm.ES256;

    }

}
