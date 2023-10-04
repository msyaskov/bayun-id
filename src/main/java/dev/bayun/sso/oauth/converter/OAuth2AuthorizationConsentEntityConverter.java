package dev.bayun.sso.oauth.converter;

import dev.bayun.sso.core.convert.EntityConverter;
import dev.bayun.sso.oauth.entity.OAuth2AuthorizationConsentEntity;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Максим Яськов
 */

@Component
public class OAuth2AuthorizationConsentEntityConverter implements EntityConverter<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> {

    private final RegisteredClientRepository registeredClientRepository;

    public OAuth2AuthorizationConsentEntityConverter(RegisteredClientRepository registeredClientRepository) {
        Assert.notNull(registeredClientRepository, "a registeredClientRepository must not be null");
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public OAuth2AuthorizationConsentEntity convertToEntity(OAuth2AuthorizationConsent authorizationConsent) {
        OAuth2AuthorizationConsentEntity entity = new OAuth2AuthorizationConsentEntity();
        entity.setRegisteredClientId(authorizationConsent.getRegisteredClientId());
        entity.setPrincipalName(authorizationConsent.getPrincipalName());

        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : authorizationConsent.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }

    @Override
    public OAuth2AuthorizationConsent convertToObject(OAuth2AuthorizationConsentEntity entity) {
        String registeredClientId = entity.getRegisteredClientId();
        RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
        }

        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
                registeredClientId, entity.getPrincipalName());
        if (entity.getAuthorities() != null) {
            for (String authority : StringUtils.commaDelimitedListToSet(entity.getAuthorities())) {
                builder.authority(new SimpleGrantedAuthority(authority));
            }
        }

        return builder.build();
    }
}
