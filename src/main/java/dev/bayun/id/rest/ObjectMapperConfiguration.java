package dev.bayun.id.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.sdk.rest.core.RestObject;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Максим Яськов
 */
@Configuration
public class ObjectMapperConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(RestObject.class, RestObjectMixin.class);
        return mapper;
    }

    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.mixIn(RestObject.class, RestObjectMixin.class);
    }

}
