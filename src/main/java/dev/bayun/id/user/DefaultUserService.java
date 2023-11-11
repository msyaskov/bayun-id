package dev.bayun.id.user;

import dev.bayun.sdk.rest.core.RestDocument;
import dev.bayun.id.rest.StringId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestOperations;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Максим Яськов
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final RestOperations restOperations;

    @Override
    public Optional<User> getUserById(UUID userId) {
        try {
            RestDocument restDocument = restOperations.getForObject("/api/users/" + userId, RestDocument.class);
            User user = (User) restDocument.getObjectMap().get("user").getObject();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("Retrieved exception", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UUID> getUserIdByEmail(String email) {
        try {
            RestDocument restDocument = restOperations.getForObject("/api/users/id?email=" + email, RestDocument.class);
            StringId userId = (StringId) restDocument.getObjectMap().get("userId").getObject();
            return Optional.ofNullable(UUID.fromString(userId.getId()));
        } catch (Exception e) {
            log.error("Retrieved exception", e);
            return Optional.empty();
        }
    }

    @Override
    public UUID register(User candidate) {
        try {
            RestDocument restDocument = restOperations.postForObject("/api/users", candidate, RestDocument.class);
            StringId userId = (StringId) restDocument.getObjectMap().get("userId").getObject();
            return UUID.fromString(userId.getId());
        } catch (Exception e) {
            throw new RuntimeException("Retrieved exception", e);
        }
    }

    @Override
    public void update(UUID userId, String nickname, String givenName, String familyName) {
        return;
    }

}
