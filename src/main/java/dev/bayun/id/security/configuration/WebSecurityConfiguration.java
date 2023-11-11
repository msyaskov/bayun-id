package dev.bayun.id.security.configuration;

import dev.bayun.id.security.oauth.configuration.SocialLoginConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

/**
 * Конфигурация web-безопасности.
 * @author Максим Яськов
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(HttpMethod.GET, "/login", "/userinfo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/assets/**").permitAll()
                        .anyRequest().authenticated()
        );

        SocialLoginConfiguration socialLoginConfiguration = new SocialLoginConfiguration();
        http.apply(socialLoginConfiguration);

        http.oauth2Login(customizer -> {
            customizer.loginPage("/login");
        });
        http.logout(customizer -> {
            customizer.logoutRequestMatcher(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/logout"));
            customizer.logoutSuccessUrl("/login");
        });
        http.formLogin(AbstractHttpConfigurer::disable);
        http.exceptionHandling(configurer -> configurer.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new NegatedRequestMatcher(new AntPathRequestMatcher("/oauth2/**"))));


        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}

