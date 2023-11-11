package dev.bayun.id.user;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Максим Яськов
 */
public interface UserService {

    Optional<User> getUserById(UUID userId);

    Optional<UUID> getUserIdByEmail(String email);

    void update(UUID userId, String nickname, String givenName, String familyName);

     UUID register(User candidate);

}
