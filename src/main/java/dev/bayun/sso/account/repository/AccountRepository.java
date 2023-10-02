package dev.bayun.sso.account.repository;

import dev.bayun.sso.account.Account;

import java.util.UUID;

public interface AccountRepository {

    Account getByEmail(String email);

    Account getById(UUID id);

    Account save(Account entity);

}
