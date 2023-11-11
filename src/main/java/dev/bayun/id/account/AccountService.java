package dev.bayun.id.account;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Максим Яськов
 */
public interface AccountService {

    Account register(Account candidate);

    Optional<Account> findById(UUID accountId);

}
