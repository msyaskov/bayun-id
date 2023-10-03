package dev.bayun.sso.account.repository;

import dev.bayun.sso.account.Account;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MapAccountRepository implements AccountRepository {

    private final Map<UUID, Account> entityIdMap = new HashMap<>();
    private final Map<String, Account> entityEmailMap = new HashMap<>();

    @Override
    public Optional<Account> findByEmail(String email) {
        Assert.notNull(email, "An email must not be null");
        return Optional.ofNullable(entityEmailMap.get(email));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        Assert.notNull(id, "An id must not be null");
        return Optional.ofNullable(entityIdMap.get(id));
    }

    @Override
    public Account save(Account entity) {
        Assert.notNull(entity, "An entity must not be null");
        Assert.notNull(entity.getEmail(), "An entity.email must not be null");
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }

        entityIdMap.put(entity.getId(), entity);
        entityEmailMap.put(entity.getEmail(), entity);

        return entity;
    }
}
