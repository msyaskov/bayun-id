package dev.bayun.sso.account.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(AccountRepository.class)
public class AccountRepositoryConfiguration {

    @Bean
    public AccountRepository mapAccountRepository() {
        return new MapAccountRepository();
    }

}
