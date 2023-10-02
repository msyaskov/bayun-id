package dev.bayun.sso.account.repository;

import dev.bayun.sso.account.AccountEntity;

import java.util.UUID;

public interface AccountRepository {

    AccountEntity getByEmail(String email);

    AccountEntity getById(UUID id);

    AccountEntity save(AccountEntity entity);

}
