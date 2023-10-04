package dev.bayun.sso.security.oauth.service;

import dev.bayun.sso.security.oauth.entity.OAuth2AuthorizationConsentEntity;
import dev.bayun.sso.core.mapper.EntityMapper;
import dev.bayun.sso.security.oauth.repository.OAuth2AuthorizationConsentEntityRepository;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Максим Яськов
 */

@Service
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final EntityMapper<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> entityMapper;

    private final OAuth2AuthorizationConsentEntityRepository authorizationConsentEntityRepository;

    public JpaOAuth2AuthorizationConsentService(EntityMapper<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> entityMapper,
                                                OAuth2AuthorizationConsentEntityRepository authorizationConsentEntityRepository) {
        Assert.notNull(entityMapper, "an entityMapper must not be null");
        Assert.notNull(entityMapper, "an authorizationConsentEntityRepository must not be null");
        this.entityMapper = entityMapper;
        this.authorizationConsentEntityRepository = authorizationConsentEntityRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "an authorizationConsent must not be null");
        this.authorizationConsentEntityRepository.save(entityMapper.toEntity(authorizationConsent));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "an authorizationConsent must not be null");
        this.authorizationConsentEntityRepository.deleteByRegisteredClientIdAndPrincipalName(
                authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "a registeredClientId must not be empty");
        Assert.hasText(principalName, "a principalName must not be empty");
        return this.authorizationConsentEntityRepository.findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName)
                .map(entityMapper::toObject)
                .orElse(null);
    }
}
