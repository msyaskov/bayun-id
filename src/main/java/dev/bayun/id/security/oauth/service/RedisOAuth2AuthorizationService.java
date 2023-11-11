package dev.bayun.id.security.oauth.service;

import dev.bayun.id.security.oauth.entity.OAuth2AuthorizationEntity;
import dev.bayun.id.security.oauth.repository.OAuth2AuthorizationEntityRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;

/**
 * @author Максим Яськов
 */

public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final OAuth2AuthorizationEntityRepository authorizationEntityRepository;

    private final Converter<OAuth2AuthorizationEntity, OAuth2Authorization> toOAuth2AuthorizationConverter;
    private final Converter<OAuth2Authorization, OAuth2AuthorizationEntity> toOAuth2AuthorizationEntityConverter;

    public RedisOAuth2AuthorizationService(OAuth2AuthorizationEntityRepository authorizationRepository,
                                           Converter<OAuth2AuthorizationEntity, OAuth2Authorization> toOAuth2AuthorizationConverter,
                                           Converter<OAuth2Authorization, OAuth2AuthorizationEntity> toOAuth2AuthorizationEntityConverter) {
        Assert.notNull(authorizationRepository, "authorizationRepository cannot be null");
        this.authorizationEntityRepository = authorizationRepository;

        Assert.notNull(toOAuth2AuthorizationConverter, "toOAuth2AuthorizationConverter cannot be null");
        this.toOAuth2AuthorizationConverter = toOAuth2AuthorizationConverter;

        Assert.notNull(toOAuth2AuthorizationEntityConverter, "toOAuth2AuthorizationEntityConverter cannot be null");
        this.toOAuth2AuthorizationEntityConverter = toOAuth2AuthorizationEntityConverter;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.authorizationEntityRepository.save(Objects.requireNonNull(toOAuth2AuthorizationEntityConverter.convert(authorization)));
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.authorizationEntityRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.authorizationEntityRepository.findById(id).map(toOAuth2AuthorizationConverter::convert).orElse(null);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        Map<String, Function<String, Optional<OAuth2AuthorizationEntity>>> map = new HashMap<>();
        map.put(OAuth2ParameterNames.STATE, this.authorizationEntityRepository::findByState);
        map.put(OAuth2ParameterNames.CODE, this.authorizationEntityRepository::findByAuthorizationCodeValue);
        map.put(OAuth2ParameterNames.ACCESS_TOKEN, this.authorizationEntityRepository::findByAccessTokenValue);
        map.put(OAuth2ParameterNames.REFRESH_TOKEN, this.authorizationEntityRepository::findByRefreshTokenValue);
        map.put(OidcParameterNames.ID_TOKEN, this.authorizationEntityRepository::findByOidcIdTokenValue);
        map.put(OAuth2ParameterNames.USER_CODE, this.authorizationEntityRepository::findByUserCodeValue);
        map.put(OAuth2ParameterNames.DEVICE_CODE, this.authorizationEntityRepository::findByDeviceCodeValue);

        Optional<OAuth2AuthorizationEntity> result = null;
        if (tokenType == null) {
            for (var stringFunctionEntry : map.entrySet()) {
                result = stringFunctionEntry.getValue().apply(token);
                if (result.isPresent()) {
                    break;
                }
            }
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByAuthorizationCodeValue(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByAccessTokenValue(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByRefreshTokenValue(token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByOidcIdTokenValue(token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByUserCodeValue(token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            result = this.authorizationEntityRepository.findByDeviceCodeValue(token);
        } else {
            result = Optional.empty();
        }

        return result.map(toOAuth2AuthorizationConverter::convert).orElse(null);
    }
}
