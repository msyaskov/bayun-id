package dev.bayun.sso.mvc.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;

/**
 * @author Максим Яськов
 */

@Configuration
public class FormContentFilterConfiguration {

    @Bean
    public FilterRegistrationBean<FormContentFilter> httpPutFormContentFilterRegistrationBean() {
        FormContentFilter filter = new FormContentFilter();
        return new FilterRegistrationBean<>(filter);
    }

}
