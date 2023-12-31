package dev.bayun.id.security.oauth.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class TokenInfoDto {

    private Boolean active;
    private String sub;
    private List<String> aud;
    private Instant nbf;
    private List<String> scopes;
    private URL iss;
    private Instant exp;
    private Instant iat;
    private String jti;
    private String clientId;
    private String tokenType;

    private Object principal;
    private Collection<? extends GrantedAuthority> authorities;

}
