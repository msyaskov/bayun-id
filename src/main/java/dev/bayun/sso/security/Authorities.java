package dev.bayun.sso.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Максим Яськов
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authorities {

    private Set<? extends GrantedAuthority> authorities;

    public static Authorities empty() {
        return new Authorities(new HashSet<>());
    }

}
