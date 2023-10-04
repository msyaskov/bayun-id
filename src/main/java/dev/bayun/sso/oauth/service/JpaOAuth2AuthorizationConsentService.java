package dev.bayun.sso.oauth.service;

import dev.bayun.sso.oauth.entity.OAuth2AuthorizationConsentEntity;
import dev.bayun.sso.core.convert.EntityConverter;
import dev.bayun.sso.oauth.repository.JpaOAuth2AuthorizationConsentEntityRepository;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Максим Яськов
 */

@Service
public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final EntityConverter<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> entityConverter;

    private final JpaOAuth2AuthorizationConsentEntityRepository authorizationConsentEntityRepository;

    public JpaOAuth2AuthorizationConsentService(EntityConverter<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> entityConverter,
                                                JpaOAuth2AuthorizationConsentEntityRepository authorizationConsentEntityRepository) {
        Assert.notNull(entityConverter, "an entityMapper must not be null");
        Assert.notNull(entityConverter, "an authorizationConsentEntityRepository must not be null");
        this.entityConverter = entityConverter;
        this.authorizationConsentEntityRepository = authorizationConsentEntityRepository;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "an authorizationConsent must not be null");
        this.authorizationConsentEntityRepository.save(entityConverter.convertToEntity(authorizationConsent));
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
                .map(entityConverter::convertToObject)
                .orElse(null);
    }
}
