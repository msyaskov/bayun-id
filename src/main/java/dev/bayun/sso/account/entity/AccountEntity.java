package dev.bayun.sso.account.entity;

import dev.bayun.sso.account.converter.AuthoritiesConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Data
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String pictureUrl;

    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;

    @Convert(converter = AuthoritiesConverter.class)
    private Authorities authorities;

    private String passwordHash;

    @Column(name = "using_2fa")
    private boolean using2FA;
    private boolean expired;
    private boolean locked;

    private boolean credentialExpired;
    private boolean enabled;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            return new HashSet<>();
        }
        
        return authorities.getAuthorities();
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (this.authorities == null) {
            this.authorities = new Authorities(new HashSet<>(authorities));
        }

        this.authorities.setAuthorities(new HashSet<>(authorities));
    }
}
