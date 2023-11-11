package dev.bayun.id.security.oauth.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.bayun.id.security.oauth.converter.OAuth2AuthorizationConsentEntityToOAuth2AuthorizationConsentConverter;
import dev.bayun.id.security.oauth.converter.OAuth2AuthorizationConsentToOAuth2AuthorizationConsentEntityConverter;
import dev.bayun.id.security.oauth.converter.OAuth2AuthorizationEntityToOAuth2AuthorizationConverter;
import dev.bayun.id.security.oauth.converter.OAuth2AuthorizationToOAuth2AuthorizationEntityConverter;
import dev.bayun.id.security.oauth.repository.OAuth2AuthorizationConsentEntityRepository;
import dev.bayun.id.security.oauth.repository.OAuth2AuthorizationEntityRepository;
import dev.bayun.id.security.oauth.service.OidcUserInfoService;
import dev.bayun.id.security.oauth.service.RedisOAuth2AuthorizationConsentService;
import dev.bayun.id.security.oauth.service.RedisOAuth2AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * Конфигурация сервера OAuth-авторизации.
 * @author Максим Яськов
 */

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class AuthorizationServerConfiguration {

    private final AuthorizationServerProperties authorizationServerProperties;

    private final RegisteredClientRepository registeredClientRepository;

    @Bean
    @Order(0)
    public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http, OAuth2AuthorizationService oAuth2AuthorizationService) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http.exceptionHandling(configurer -> configurer.defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

        http.oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(OidcUserInfoService userInfoService) {
        return (context) -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                OidcUserInfo userInfo = userInfoService.loadUser(
                        UUID.fromString(context.getPrincipal().getName()));
                context.getClaims().claims(claims ->
                        claims.putAll(userInfo.getClaims()));
            }
        };
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .build();
    }

    @Bean
    public OAuth2AuthorizationService redisOAuth2AuthorizationService(@Qualifier("oAuth2ObjectMapper") ObjectMapper objectMapper,
                                                                      OAuth2AuthorizationEntityRepository authorizationEntityRepository) {
        return new RedisOAuth2AuthorizationService(
                authorizationEntityRepository,
                new OAuth2AuthorizationEntityToOAuth2AuthorizationConverter(registeredClientRepository, objectMapper),
                new OAuth2AuthorizationToOAuth2AuthorizationEntityConverter(objectMapper));
    }

    @Bean
    public OAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService(OAuth2AuthorizationConsentEntityRepository authorizationConsentEntityRepository) {
        return new RedisOAuth2AuthorizationConsentService(
                authorizationConsentEntityRepository,
                new OAuth2AuthorizationConsentEntityToOAuth2AuthorizationConsentConverter(registeredClientRepository),
                new OAuth2AuthorizationConsentToOAuth2AuthorizationConsentEntityConverter());
    }

}
