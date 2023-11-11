package dev.bayun.id.security.oauth.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Максим Яськов
 */

@Data
@RedisHash("oauth2:authorization:consent")
public class OAuth2AuthorizationConsentEntity {

    private String id;

    @Transient
    private String registeredClientId;

    @Transient
    private String principalName;

    private String authorities;

    public static String getIdOf(String registeredClientId, String principalName) {
        return String.format("%s:%s", registeredClientId, principalName);
    }

    public void setId(String id) {
        this.id = id;

        if (id != null) {
            String[] split = id.split(",", 2);
            this.registeredClientId = split[0];
            this.principalName = split[1];
        }
    }

    @Id
    public String getId() {
        return this.id;
    }
}
