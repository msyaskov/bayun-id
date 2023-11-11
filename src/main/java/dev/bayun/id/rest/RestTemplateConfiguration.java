package dev.bayun.id.rest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

/**
 * @author Максим Яськов
 */
@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    @LoadBalanced
    public RestOperations msUsersRestOperations(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setInterceptors(List.of(new BasicAuthenticationInterceptor("developer", "password")));
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://ms-users"));
        return restTemplate;
    }

}
