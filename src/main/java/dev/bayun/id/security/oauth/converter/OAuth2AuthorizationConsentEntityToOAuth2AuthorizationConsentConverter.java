package dev.bayun.id.security.oauth.converter;

import dev.bayun.id.security.oauth.entity.OAuth2AuthorizationConsentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

/**
 * @author Максим Яськов
 */
@RequiredArgsConstructor
public class OAuth2AuthorizationConsentEntityToOAuth2AuthorizationConsentConverter
        implements Converter<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsent> {

    private final RegisteredClientRepository registeredClientRepository;

    @Override
    public OAuth2AuthorizationConsent convert(OAuth2AuthorizationConsentEntity entity) {
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
