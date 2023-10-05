package dev.bayun.sso.security.configuration;

import dev.bayun.sso.oauth.configuration.SocialLoginConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация web-безопасности.
 * @author Максим Яськов
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(HttpMethod.GET, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/assets/**").permitAll()
                        .anyRequest().authenticated()
        );

        SocialLoginConfiguration socialLoginConfiguration = new SocialLoginConfiguration();
        http.apply(socialLoginConfiguration);

        http.oauth2Login(customizer -> {
            customizer.loginPage("/login");
        });
        http.logout(customizer -> {
            customizer.logoutRequestMatcher(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/logout"));
        });
        http.formLogin(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}

