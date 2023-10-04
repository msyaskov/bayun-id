package dev.bayun.sso.oauth.repository;

import dev.bayun.sso.oauth.entity.OAuth2AuthorizationConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Максим Яськов
 */

@Repository
public interface JpaOAuth2AuthorizationConsentEntityRepository extends JpaRepository<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsentEntity.OAuth2AuthorizationConsentEntityId> {

    Optional<OAuth2AuthorizationConsentEntity> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

}
