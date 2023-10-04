package dev.bayun.sso.oauth.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.sso.core.convert.EntityConverter;
import dev.bayun.sso.oauth.entity.RegisteredClientEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Максим Яськов
 */

@Component
public class RegisteredClientEntityConverter implements EntityConverter<RegisteredClientEntity, RegisteredClient> {

    @Qualifier("oAuth2ObjectMapper")
    private final ObjectMapper objectMapper;

    public RegisteredClientEntityConverter(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "an objectMapper must not be null");
        this.objectMapper = objectMapper;
    }

    @Override
    public RegisteredClientEntity convertToEntity(RegisteredClient registeredClient) {
        List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
        registeredClient.getClientAuthenticationMethods().forEach(clientAuthenticationMethod ->
                clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));

        List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
        registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
                authorizationGrantTypes.add(authorizationGrantType.getValue()));

        RegisteredClientEntity entity = new RegisteredClientEntity();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
        entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
        entity.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
        entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));

        try {
            entity.setClientSettings(this.objectMapper.writeValueAsString(registeredClient.getClientSettings().getSettings()));
            entity.setTokenSettings(this.objectMapper.writeValueAsString(registeredClient.getTokenSettings().getSettings()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return entity;
    }

    @Override
    public RegisteredClient convertToObject(RegisteredClientEntity entity) {
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(entity.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(entity.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(entity.getRedirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(entity.getPostLogoutRedirectUris());
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(entity.getScopes());

        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName())
                .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes));

        try {
            Map<String, Object> clientSettingsMap = this.objectMapper.readValue(entity.getClientSettings(),
                    new TypeReference<Map<String, Object>>() {});
            builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

            Map<String, Object> tokenSettingsMap = this.objectMapper.readValue(entity.getTokenSettings(),
                    new TypeReference<Map<String, Object>>() {});
            builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return builder.build();
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equalsIgnoreCase(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equalsIgnoreCase(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equalsIgnoreCase(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }

        return new AuthorizationGrantType(authorizationGrantType);
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }

        return new ClientAuthenticationMethod(clientAuthenticationMethod);
    }
}
