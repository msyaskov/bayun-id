package dev.bayun.sso.oauth.configuration;

import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.sso.security.AuthorizedUser;
import dev.bayun.sso.account.repository.AccountRepository;
import dev.bayun.sso.oauth.dto.PrincipalDto;
import dev.bayun.sso.oauth.dto.TokenInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Optional;

/**
 * Конфигурация сервера OAuth-авторизации.
 * @author Максим Яськов
 */

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class AuthorizationServerConfiguration {

    private final static String principalAttributeKey = "java.security.Principal";

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    private final AuthorizationServerProperties authorizationServerProperties;

    private final AccountRepository accountRepository;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final Converter<AccountEntity, PrincipalDto> accountEntityToPrincipalDtoConverter;

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
                    accountRepository.findById(((AuthorizedUser) attributeAuth.getPrincipal()).getAccountId())
                            .map(accountEntityToPrincipalDtoConverter::convert)
                            .ifPresent(principalDto -> tokenInfoDtoBuilder.principal(principalDto)
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
