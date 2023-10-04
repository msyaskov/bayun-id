package dev.bayun.sso.oauth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Дополнительные свойства сервера OAuth-авторизации.
 * @author Максим Яськов
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.authorizationserver")
class AuthorizationServerProperties {

    private String issuerUrl;
    private String introspectionEndpoint;

}
