package dev.bayun.sso.security.oauth.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.sso.account.Account;
import dev.bayun.sso.account.AuthorizedUser;
import dev.bayun.sso.account.AuthorizedUserMixin;
import dev.bayun.sso.account.repository.AccountRepository;
import dev.bayun.sso.dto.PrincipalDto;
import dev.bayun.sso.dto.TokenInfoDto;
import dev.bayun.sso.security.InMemoryRegisteredClientRepositoryConfiguration;
import dev.bayun.sso.security.oauth.service.JpaOAuth2AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Конфигурация сервера OAuth-авторизации.
 * @author Максим Яськов
 */

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthorizationServerProperties.class)
@Import(InMemoryRegisteredClientRepositoryConfiguration.class)
public class AuthorizationServerConfiguration {

    private final static String principalAttributeKey = "java.security.Principal";

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    private final AuthorizationServerProperties authorizationServerProperties;

    private final AccountRepository accountRepository;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Bean
    public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.tokenIntrospectionEndpoint((config) -> {
            config.introspectionResponseHandler(this::introspectionResponse);
        });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    private void introspectionResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2TokenIntrospectionAuthenticationToken introspectionAuthenticationToken = (OAuth2TokenIntrospectionAuthenticationToken) authentication;
        TokenInfoDto.TokenInfoDtoBuilder tokenInfoDtoBuilder = TokenInfoDto.builder().active(false);                        // создаём билдер объекта ответа
        if (introspectionAuthenticationToken.getTokenClaims().isActive()) {                                                 // если токен активен, то заполняем все параметры информации о токене и далее пытаемся получить информацию о пользователе
            OAuth2TokenIntrospection claims = introspectionAuthenticationToken.getTokenClaims();
            tokenInfoDtoBuilder.active(true)
                    .sub(claims.getSubject())
                    .aud(claims.getAudience())
                    .nbf(claims.getNotBefore())
                    .scopes(claims.getScopes())
                    .iss(claims.getIssuer())
                    .exp(claims.getExpiresAt())
                    .iat(claims.getIssuedAt())
                    .jti(claims.getId())
                    .clientId(claims.getClientId())
                    .tokenType(claims.getTokenType());


            String token = introspectionAuthenticationToken.getToken();                                                     // получаем значение токена, который проверяется
            OAuth2Authorization tokenAuth = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);    // предполагая что это ACCESS TOKEN, пытаемся получить объект OAuth2Authorization из OAuth2AuthorizationService
            if (tokenAuth != null) {
                Authentication attributeAuth = tokenAuth.getAttribute(principalAttributeKey);                               // Если найден этот объект OAuth2Authorization, то получаем из него объект Authentication следующим образом
                if (attributeAuth != null) {
                    Optional<Account> byId = accountRepository.findById(((AuthorizedUser) attributeAuth.getPrincipal()).getAccountId());
                    accountRepository.findById(((AuthorizedUser) attributeAuth.getPrincipal()).getAccountId())
                            .ifPresent(a -> tokenInfoDtoBuilder.principal(PrincipalDto.builder()
                                            .id(a.getId())
                                            .email(a.getEmail())
                                            .firstName(a.getFirstName())
                                            .lastName(a.getLastName())
                                            .pictureUrl(a.getPictureUrl())
                                            .build())
                                    .authorities(authentication.getAuthorities()));
                }
            }
        }

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        mappingJackson2HttpMessageConverter.write(tokenInfoDtoBuilder.build(), null, httpResponse);                         // Предращаем наш TokenInfoDto в json строку и отправляем её через ServletServerHttpResponse
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationServerProperties.getIssuerUrl())
                .tokenIntrospectionEndpoint(authorizationServerProperties.getIntrospectionEndpoint())
                .build();
    }

//    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

}
