package dev.bayun.sso.oauth.configuration;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.bayun.sso.security.AuthorizedUser;
import dev.bayun.sso.security.AuthorizedUserMixin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;

/**
 * @author Максим Яськов
 */

@Configuration
public class OAuth2ObjectMapperConfiguration {

    @Bean(name = "oAuth2ObjectMapper")
    public ObjectMapper oAuth2ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        ClassLoader classLoader = AuthorizationServerConfiguration.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
//        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        objectMapper.addMixIn(AuthorizedUser.class, AuthorizedUserMixin.class);

        return objectMapper;
    }
}
