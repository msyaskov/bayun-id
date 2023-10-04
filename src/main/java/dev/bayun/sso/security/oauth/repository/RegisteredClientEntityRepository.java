package dev.bayun.sso.security.oauth.repository;

import dev.bayun.sso.security.oauth.entity.RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Максим Яськов
 */

@Repository
public interface RegisteredClientEntityRepository extends JpaRepository<RegisteredClientEntity, String> {

    Optional<RegisteredClientEntity> findByClientId(String clientId);

}
