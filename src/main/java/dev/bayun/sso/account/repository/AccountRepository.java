package dev.bayun.sso.account.repository;

import dev.bayun.sso.account.entity.AccountEntity;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findById(UUID id);

    AccountEntity save(AccountEntity entity);

}
