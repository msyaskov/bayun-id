package dev.bayun.id.security.oauth.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2RegisteredClientProperties.class)
public class OAuth2RegisteredClientConfiguration {

    private final OAuth2RegisteredClientProperties registeredClientProperties;

    private final PasswordEncoder passwordEncoder;

//    @EventListener(ApplicationStartedEvent.class)
//    @Profile("oauth2-test-client")
//    public void onApplicationStarted(ApplicationStartedEvent event) {
//        registerTestClient();
//        registerApiGatewayClient();
//        registerVoteWebClient();
//    }

    @Bean
    public RegisteredClientRepository inMemoryRegisteredClientRepository() {
        List<RegisteredClient> registeredClients = registeredClientProperties.getClient().entrySet().stream().map(entry -> {
            var clientName = entry.getKey();
            var clientProperties = entry.getValue();

            return RegisteredClient.withId(clientName)
                    .clientName(clientName)
                    .clientId(clientProperties.getClientId())
                    .clientIdIssuedAt(Instant.now())
                    .clientSecret(passwordEncoder.encode(clientProperties.getClientSecret()))
                    .clientAuthenticationMethods(clientAuthenticationMethods -> {
                        if (clientProperties.getClientAuthenticationMethod() != null) {
                            clientAuthenticationMethods.add(new ClientAuthenticationMethod(clientProperties.getClientAuthenticationMethod().toLowerCase()));
                        }
                    })
                    .authorizationGrantTypes(authorizationGrantTypes -> {
                        if (clientProperties.getAuthorizationGrantType() != null) {
                            authorizationGrantTypes.addAll(clientProperties.getAuthorizationGrantType().stream().map(String::toLowerCase).map(AuthorizationGrantType::new).toList());
                        }
                    })
                    .scopes(scopes -> {
                        if (clientProperties.getScope() != null) {
                            scopes.addAll(clientProperties.getScope());
                        }
                    })
                    .redirectUris(uris -> uris.addAll(clientProperties.getRedirectUri()))
                    .tokenSettings(clientProperties.getToken() == null ? defaultTokenSettings() : TokenSettings.builder()
                            .accessTokenFormat(new OAuth2TokenFormat(clientProperties.getToken().getAccessTokenFormat()))
                            .accessTokenTimeToLive(clientProperties.getToken().getAccessTokenLifetime())
                            .refreshTokenTimeToLive(clientProperties.getToken().getRefreshTokenLifetime())
                            .reuseRefreshTokens(false)
                            .authorizationCodeTimeToLive(clientProperties.getToken().getAuthorizationCodeLifetime())
                            .deviceCodeTimeToLive(clientProperties.getToken().getDeviceCodeLifetime())
                            .build())
                    .build();
        }).toList();

        return new InMemoryRegisteredClientRepository(registeredClients);
    }

    private TokenSettings defaultTokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.of(30, ChronoUnit.MINUTES))
                .refreshTokenTimeToLive(Duration.of(12, ChronoUnit.HOURS))
                .reuseRefreshTokens(false)
                .authorizationCodeTimeToLive(Duration.of(30, ChronoUnit.SECONDS))
                .build();
    }

    private void registerTestClient() {
//        RegisteredClient savedClient = registeredClientRepository.findByClientId("test-client");
//        if (savedClient == null) {
//            registeredClientRepository.save(RegisteredClient.withId("test-client-id")
//                    .clientName("Test Client")
//                    .clientId("test-client")
//                    .clientIdIssuedAt(Instant.now())
//                    .clientSecret(passwordEncoder.encode("test-client"))
//                    .redirectUri("http://localhost:8080/code")
//                    .scope("profile")
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                    .tokenSettings(defaultTokenSettings())
//                    .build());
//        }

    }

    private void registerApiGatewayClient() {
//        RegisteredClient savedClient = registeredClientRepository.findByClientId("bayun-api-gateway-oauth-client");
//        if (savedClient == null) {
//            registeredClientRepository.save(RegisteredClient.withId("bayun-api-gateway-oauth-client-id")
//                    .clientName("Test Client")
//                    .clientId("bayun-api-gateway-oauth-client")
//                    .clientIdIssuedAt(Instant.now())
//                    .clientSecret(passwordEncoder.encode("secret"))
//                    .redirectUri("http://api.bayun.localhost:8082/login/oauth2/code/bayun-api-gateway-oauth-client")
//                    .scope("scope_pro_file")
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .tokenSettings(defaultTokenSettings())
//                    .build());
//        }

    }

    private void registerVoteWebClient() {
//        RegisteredClient savedClient = registeredClientRepository.findByClientId("bayun-sso-vote-web-client");
//        if (savedClient == null) {
//            registeredClientRepository.save(RegisteredClient.withId("bayun-sso-vote-web-client-id")
//                    .clientName("BayunVoteWebClient")
//                    .clientId("bayun-sso-vote-web-client")
//                    .clientIdIssuedAt(Instant.now())
//                    .clientSecret(passwordEncoder.encode("secret"))
//                    .redirectUri("http://vote.bayun.localhost:8091/login/oauth2/code/bayun-sso-vote-web-client")
//                    .scope("sso_profile")
//                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                    .tokenSettings(defaultTokenSettings())
//                    .build());
//        }

    }



}
