package dev.bayun.sso.account;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class AuthorizedUser extends User implements OAuth2User {

    public AuthorizedUser(UUID id, String password, Collection<? extends GrantedAuthority> authorities,
            boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, boolean enabled) {
        super(id.toString(), password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UUID getAccountId() {
        return UUID.fromString(this.getUsername());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}
