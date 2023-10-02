package dev.bayun.sso.security;

import lombok.Setter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Конфигуратор социального входа (через google, yandex и т.д.)
 * @author Максим Яськов
 */

@Setter
public class SocialLoginConfiguration extends AbstractHttpConfigurer<SocialLoginConfiguration, HttpSecurity> {

    private AuthenticationFailureHandler failureHandler;
    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.oauth2Login(configurer -> {
            if (failureHandler != null) {
                configurer.failureHandler(failureHandler);
            }
            if (successHandler != null) {
                configurer.successHandler(this.successHandler);
            }
        });
    }

}
