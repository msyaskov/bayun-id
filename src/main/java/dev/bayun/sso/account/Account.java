package dev.bayun.sso.account;

import dev.bayun.sso.core.entity.DefaultCoreEntity;
import dev.bayun.sso.security.Authorities;
import dev.bayun.sso.util.converter.AuthoritiesConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Account extends DefaultCoreEntity<UUID> implements UserDetails {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String pictureUrl;

    @Convert(converter = AuthoritiesConverter.class)
    private Authorities authorities;

    private String password;

    @Column(name = "using_2fa")
    private boolean using2FA;
    private boolean expired;
    private boolean locked;

    private boolean credentialExpired;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
