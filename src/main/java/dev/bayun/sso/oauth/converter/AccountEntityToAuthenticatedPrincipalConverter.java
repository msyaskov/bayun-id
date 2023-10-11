package dev.bayun.sso.oauth.converter;

import dev.bayun.api.sso.AuthenticatedPrincipal;
import dev.bayun.sso.account.entity.AccountEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Максим Яськов
 */

@Component
public class AccountEntityToAuthenticatedPrincipalConverter implements Converter<AccountEntity, AuthenticatedPrincipal> {

    @Override
    public AuthenticatedPrincipal convert(AccountEntity source) {
        return AuthenticatedPrincipal.builder()
                .id(source::getId)
                .username(source::getUsername)
                .passwordHash(source::getPasswordHash)
                .email(source::getEmail)
                .attributes(attributes -> {})
                .firstName(source::getFirstName)
                .lastName(source::getLastName)
                .pictureUrl(source::getPictureUrl)
                .expired(source::isExpired)
                .locked(source::isLocked)
                .credentialsExpired(source::isCredentialExpired)
                .enabled(source::isEnabled)
                .authorities(authorities -> authorities.addAll(source.getAuthorities()))
                .build();
    }
}
