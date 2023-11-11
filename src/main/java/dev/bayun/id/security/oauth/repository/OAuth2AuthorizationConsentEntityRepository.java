package dev.bayun.id.security.oauth.repository;

import dev.bayun.id.security.oauth.entity.OAuth2AuthorizationConsentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Максим Яськов
 */

@Repository
public interface OAuth2AuthorizationConsentEntityRepository extends CrudRepository<OAuth2AuthorizationConsentEntity, String> {

}
