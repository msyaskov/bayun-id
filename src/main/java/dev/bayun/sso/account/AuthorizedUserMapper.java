package dev.bayun.sso.account;

import java.util.Objects;

public class AuthorizedUserMapper {

    public static AuthorizedUser map(Account entity) {
        return new AuthorizedUser(entity.getId(),
                Objects.requireNonNullElse(entity.getPassword(), ""),
                entity.getAuthorities(),
                entity.isAccountNonExpired(),
                entity.isCredentialsNonExpired(),
                entity.isAccountNonLocked(),
                entity.isEnabled());
    }

}
