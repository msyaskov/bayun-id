package dev.bayun.sso.security.oauth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Максим Яськов
 */

@Data
@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(OAuth2AuthorizationConsentEntity.OAuth2AuthorizationConsentEntityId.class)
public class OAuth2AuthorizationConsentEntity {

    @Id
    private String registeredClientId;

    @Id
    private String principalName;

    @Column(length = 1000)
    private String authorities;

    public static class OAuth2AuthorizationConsentEntityId implements Serializable {

        private String registeredClientId;

        private String principalName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OAuth2AuthorizationConsentEntityId that = (OAuth2AuthorizationConsentEntityId) o;
            return registeredClientId.equals(that.registeredClientId) && principalName.equals(that.principalName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registeredClientId, principalName);
        }
    }

}
