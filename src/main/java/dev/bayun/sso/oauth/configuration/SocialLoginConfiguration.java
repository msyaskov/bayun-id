package dev.bayun.sso.oauth.configuration;

import lombok.Setter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.oauth2Login(configurer -> {
            if (this.oAuth2UserService != null) {
                configurer.userInfoEndpoint(customizer -> {
                    customizer.userService(this.oAuth2UserService);
                });
            }
            if (failureHandler != null) {
                configurer.failureHandler(failureHandler);
            }
            if (successHandler != null) {
                configurer.successHandler(this.successHandler);
            }
        });
    }

}
