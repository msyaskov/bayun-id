package dev.bayun.sso.account.repository;

import dev.bayun.sso.account.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Optional<Account> findByEmail(String email);

    Optional<Account> findById(UUID id);

    Account save(Account entity);

}
