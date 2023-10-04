package dev.bayun.sso.oauth.converter;

import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.sso.security.AuthorizedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccountEntityToAuthorizedUserConverter implements Converter<AccountEntity, AuthorizedUser> {

    @Override
    public AuthorizedUser convert(AccountEntity source) {
        return new AuthorizedUser(source.getId(),
                Objects.requireNonNullElse(source.getPasswordHash(), ""),
                source.getAuthorities(),
                !source.isExpired(),
                !source.isCredentialExpired(),
                !source.isLocked(),
                source.isEnabled());
    }
}
