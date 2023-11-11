package dev.bayun.id.security.oauth.repository;

import dev.bayun.id.security.oauth.entity.OAuth2AuthorizationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Максим Яськов
 */
@Repository
public interface OAuth2AuthorizationEntityRepository extends CrudRepository<OAuth2AuthorizationEntity, String> {

    Optional<OAuth2AuthorizationEntity> findByState(String state);
    Optional<OAuth2AuthorizationEntity> findByAuthorizationCodeValue(String authorizationCode);
    Optional<OAuth2AuthorizationEntity> findByAccessTokenValue(String accessToken);
    Optional<OAuth2AuthorizationEntity> findByRefreshTokenValue(String refreshToken);
    Optional<OAuth2AuthorizationEntity> findByOidcIdTokenValue(String idToken);
    Optional<OAuth2AuthorizationEntity> findByUserCodeValue(String userCode);
    Optional<OAuth2AuthorizationEntity> findByDeviceCodeValue(String deviceCode);

}
