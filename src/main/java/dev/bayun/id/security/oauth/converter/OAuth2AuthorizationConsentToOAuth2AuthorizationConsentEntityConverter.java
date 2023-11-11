package dev.bayun.id.security.oauth.converter;

import dev.bayun.id.security.oauth.entity.OAuth2AuthorizationConsentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Максим Яськов
 */
@RequiredArgsConstructor
public class OAuth2AuthorizationConsentToOAuth2AuthorizationConsentEntityConverter
        implements Converter<OAuth2AuthorizationConsent, OAuth2AuthorizationConsentEntity> {

    @Override
    public OAuth2AuthorizationConsentEntity convert(OAuth2AuthorizationConsent source) {
        OAuth2AuthorizationConsentEntity entity = new OAuth2AuthorizationConsentEntity();
        entity.setRegisteredClientId(source.getRegisteredClientId());
        entity.setPrincipalName(source.getPrincipalName());

        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : source.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }
        entity.setAuthorities(StringUtils.collectionToCommaDelimitedString(authorities));

        return entity;
    }
}
