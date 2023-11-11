package dev.bayun.id.principal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Максим Яськов
 */
public final class OwnPrincipal extends DefaultOAuth2User implements UserDetails, CredentialsContainer {

    private static final String DEFAULT_ID_ATTRIBUTE_KEY = "sub";
    private static final String DEFAULT_ACCOUNT_LOCKED_ATTRIBUTE_KEY = "locked";
    private static final String DEFAULT_ACCOUNT_ENABLED_ATTRIBUTE_KEY = "enabled";

    private String password;

    @JsonCreator
    public OwnPrincipal(
            @JsonProperty("password") String password,
            @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
            @JsonProperty("attributes") Map<String, Object> attributes) {
        super(authorities, attributes, DEFAULT_ID_ATTRIBUTE_KEY);
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() { // this is principal id (not a username)
        return getAttribute(DEFAULT_ID_ATTRIBUTE_KEY);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(getAttribute(DEFAULT_ACCOUNT_LOCKED_ATTRIBUTE_KEY));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !Boolean.TRUE.equals(getAttribute(DEFAULT_ACCOUNT_ENABLED_ATTRIBUTE_KEY));
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
    public static OwnPrincipalBuilder builder() {
        return new OwnPrincipalBuilder();
    }

    public static final class OwnPrincipalBuilder {

        private String password;

        private final Set<GrantedAuthority> authorities = new HashSet<>();

        private final Map<String, Object> attributes = new HashMap<>();

        private OwnPrincipalBuilder() {}

        public OwnPrincipalBuilder sub(String sub) {
            this.attributes.put(DEFAULT_ID_ATTRIBUTE_KEY, sub);
            return this;
        }

        public OwnPrincipalBuilder password(String password) {
            this.password = password;
            return this;
        }

        public OwnPrincipalBuilder locked(boolean locked) {
            this.attributes.put(DEFAULT_ACCOUNT_LOCKED_ATTRIBUTE_KEY, locked);
            return this;
        }

        public OwnPrincipalBuilder enabled(boolean enabled) {
            this.attributes.put(DEFAULT_ACCOUNT_ENABLED_ATTRIBUTE_KEY, enabled);
            return this;
        }

        public OwnPrincipalBuilder authority(GrantedAuthority authority) {
            this.authorities.add(authority);
            return this;
        }

        public OwnPrincipalBuilder authorities(Consumer<Collection<GrantedAuthority>> authorities) {
            authorities.accept(this.authorities);
            return this;
        }

        public OwnPrincipalBuilder attribute(String attrName, Object attrValue) {
            this.attributes.put(attrName, attrValue);
            return this;
        }

        public OwnPrincipalBuilder attributes(Consumer<Map<String, Object>> attributes) {
            attributes.accept(this.attributes);
            return this;
        }

        public OwnPrincipal build() {
            return new OwnPrincipal(password, Collections.unmodifiableCollection(authorities), attributes);
        }
    }
}
