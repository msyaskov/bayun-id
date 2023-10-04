package dev.bayun.sso.account.repository;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountRepositoryConfiguration {

//    @Bean
    public AccountRepository mapAccountRepository() {
        return new MapAccountRepository();
    }

}
