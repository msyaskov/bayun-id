package dev.bayun.sso.oauth.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
public class OAuth2RegisteredClientConfiguration {

    private final RegisteredClientRepository registeredClientRepository;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationStartedEvent.class)
    @Profile("oauth2-test-client")
    public void onApplicationStarted(ApplicationStartedEvent event) {
        registerTestClient();
        registerApiGatewayClient();
        registerVoteWebClient();
    }

    private void registerTestClient() {
        RegisteredClient savedClient = registeredClientRepository.findByClientId("test-client");
        if (savedClient == null) {
            registeredClientRepository.save(RegisteredClient.withId("test-client-id")
                    .clientName("Test Client")
                    .clientId("test-client")
                    .clientIdIssuedAt(Instant.now())
                    .clientSecret(passwordEncoder.encode("test-client"))
                    .redirectUri("http://localhost:8080/code")
                    .scope("profile")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .tokenSettings(defaultTokenSettings())
                    .build());
        }

    }

    private void registerApiGatewayClient() {
        RegisteredClient savedClient = registeredClientRepository.findByClientId("bayun-api-gateway-oauth-client");
        if (savedClient == null) {
            registeredClientRepository.save(RegisteredClient.withId("bayun-api-gateway-oauth-client-id")
                    .clientName("Test Client")
                    .clientId("bayun-api-gateway-oauth-client")
                    .clientIdIssuedAt(Instant.now())
                    .clientSecret(passwordEncoder.encode("secret"))
                    .redirectUri("http://api.bayun.localhost:8082/login/oauth2/code/bayun-api-gateway-oauth-client")
                    .scope("scope_pro_file")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .tokenSettings(defaultTokenSettings())
                    .build());
        }

    }

    private void registerVoteWebClient() {
        RegisteredClient savedClient = registeredClientRepository.findByClientId("bayun-sso-vote-web-client");
        if (savedClient == null) {
            registeredClientRepository.save(RegisteredClient.withId("bayun-sso-vote-web-client-id")
                    .clientName("BayunVoteWebClient")
                    .clientId("bayun-sso-vote-web-client")
                    .clientIdIssuedAt(Instant.now())
                    .clientSecret(passwordEncoder.encode("secret"))
                    .redirectUri("http://vote.bayun.localhost:8091/login/oauth2/code/bayun-sso-vote-web-client")
                    .scope("sso_profile")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .tokenSettings(defaultTokenSettings())
                    .build());
        }

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
