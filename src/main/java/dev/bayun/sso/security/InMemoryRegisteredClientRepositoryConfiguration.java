package dev.bayun.sso.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
public class InMemoryRegisteredClientRepositoryConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public RegisteredClientRepository inMemoryRegisteredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                RegisteredClient.withId("test-client-id")
                        .clientName("Test Client")
                        .clientId("test-client")
                        .clientSecret("$2a$10$Gp8suVFHUTV7y.y2TmtVi.W8CKW7qP65EdcNEFxO.R/2b9naLQDM.") // test-client
                        .redirectUri("http://localhost:8080/code")
                        .scope("profile")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .tokenSettings(defaultTokenSettings())
                        .build(),
                RegisteredClient.withId("oauthdebugger")
                        .clientName("oauthdebugger")
                        .clientId("oauthdebugger")
                        .clientSecret(passwordEncoder.encode("oauthdebugger"))
                        .redirectUri("https://oauthdebugger.com/debug")
                        .scope("profile")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .tokenSettings(defaultTokenSettings())
                        .build());
    }

    private TokenSettings defaultTokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                .accessTokenTimeToLive(Duration.of(30, ChronoUnit.MINUTES))
                .refreshTokenTimeToLive(Duration.of(12, ChronoUnit.HOURS))
                .reuseRefreshTokens(false)
                .authorizationCodeTimeToLive(Duration.of(30, ChronoUnit.SECONDS))
                .build();
    }

}
