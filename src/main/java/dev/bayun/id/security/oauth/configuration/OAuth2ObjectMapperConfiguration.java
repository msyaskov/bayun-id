package dev.bayun.id.security.oauth.configuration;


import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.id.principal.OwnPrincipal;
import dev.bayun.id.principal.OwnPrincipalMixin;
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
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        objectMapper.addMixIn(OwnPrincipal.class, OwnPrincipalMixin.class);

        return objectMapper;
    }
}
