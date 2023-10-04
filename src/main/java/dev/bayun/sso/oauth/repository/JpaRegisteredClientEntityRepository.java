package dev.bayun.sso.oauth.repository;

import dev.bayun.sso.oauth.entity.RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Максим Яськов
 */

@Repository
public interface JpaRegisteredClientEntityRepository extends JpaRepository<RegisteredClientEntity, String> {

    Optional<RegisteredClientEntity> findByClientId(String clientId);

}
