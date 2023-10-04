package dev.bayun.sso.security.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Конфигурация CORS-фильтра.
 * @author Максим Яськов
 */

@Configuration
public class CorsFilterConfiguration {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowCredentials(true);

        cors.addAllowedOrigin("http://localhost:8080");
        cors.addAllowedHeader(CorsConfiguration.ALL);
        cors.addExposedHeader(CorsConfiguration.ALL);
        cors.addAllowedMethod(CorsConfiguration.ALL);

        source.registerCorsConfiguration("/**", cors);
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

}
