package dev.bayun.id.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

/**
 * @author Максим Яськов
 */
@Configuration
public class DefaultUserServiceConfiguration {

    @Bean
    public UserService userService(@Qualifier("msUsersRestOperations") RestOperations restOperations) {
        return new DefaultUserService(restOperations);
    }

}
