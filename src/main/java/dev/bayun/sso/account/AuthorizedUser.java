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

    private final UUID id;

    public AuthorizedUser(UUID id, String email, Collection<? extends GrantedAuthority> authorities) {
        this(id, email, true, true, true, true, authorities);
    }

    public AuthorizedUser(UUID id, String email, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(email, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public String getEmail() {
        return this.getUsername();
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
