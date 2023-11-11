package dev.bayun.id.account;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Максим Яськов
 */
@Service
@RequiredArgsConstructor
public class DefaultAccountService implements AccountService {

    private final AccountRepository accountRepository;

    @Setter
    private Supplier<UUID> accountIdGenerator = UUID::randomUUID;

    @Setter
    private Set<String> authoritiesForNewAccount = new HashSet<>();

    public Account register(Account candidate) {
        Assert.notNull(candidate, "A candidate must not be null");

        candidate.setEnabled(true);
        candidate.setLocked(false);
        candidate.setPasswordHash(null);
        candidate.setAuthorities(authoritiesForNewAccount);

        return accountRepository.save(candidate);
    }

    public Optional<Account> findById(UUID accountId) {
        return accountRepository.findById(accountId);
    }

}
