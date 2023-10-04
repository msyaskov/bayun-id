package dev.bayun.sso.account;

import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.sso.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Максим Яськов
 */

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountEntity getById(UUID id) {
        return accountRepository.findById(id).orElse(null);
    }

    public AccountEntity save(AccountEntity account) {
        Assert.notNull(account, "an account must not be null");
        return accountRepository.save(account);
    }

}
