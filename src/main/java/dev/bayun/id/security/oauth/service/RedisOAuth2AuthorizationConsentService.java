package dev.bayun.id.security.oauth.service;

import dev.bayun.id.security.oauth.entity.OAuth2AuthorizationConsentEntity;
import dev.bayun.id.security.oauth.repository.OAuth2AuthorizationConsentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author Максим Яськов
 */

@RequiredArgsConstructor
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final OAuth2AuthorizationConsentEntityRepository authorizationConsentEntityRepository;

    private final Converter<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> toAuthorizationConsentConverter;
    private final Converter<OAuth2AuthorizationConsent, OAuth2AuthorizationConsentEntity> toAuthorizationConsentEntityConverter;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        this.authorizationConsentEntityRepository.save(Objects.requireNonNull(toAuthorizationConsentEntityConverter.convert(authorizationConsent)));
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        this.authorizationConsentEntityRepository.deleteById(OAuth2AuthorizationConsentEntity.getIdOf(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName()));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return this.authorizationConsentEntityRepository.findById(OAuth2AuthorizationConsentEntity.getIdOf(registeredClientId, principalName))
                .map(toAuthorizationConsentConverter::convert).orElse(null);
    }
}
