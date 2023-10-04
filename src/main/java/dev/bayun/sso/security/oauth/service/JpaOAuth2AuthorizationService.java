package dev.bayun.sso.security.oauth.service;

import dev.bayun.sso.security.oauth.entity.OAuth2AuthorizationEntity;
import dev.bayun.sso.core.mapper.EntityMapper;
import dev.bayun.sso.security.oauth.repository.OAuth2AuthorizationEntityRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Максим Яськов
 */

@Service
public class JpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final EntityMapper<OAuth2AuthorizationEntity, OAuth2Authorization> authorizationEntityMapper;
    private final OAuth2AuthorizationEntityRepository authorizationEntityRepository;

    public JpaOAuth2AuthorizationService(EntityMapper<OAuth2AuthorizationEntity, OAuth2Authorization> authorizationEntityMapper,
                                         OAuth2AuthorizationEntityRepository authorizationEntityRepository) {
        this.authorizationEntityMapper = authorizationEntityMapper;
        this.authorizationEntityRepository = authorizationEntityRepository;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "an authorization must not be null");
        this.authorizationEntityRepository.save(authorizationEntityMapper.toEntity(authorization));
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "an authorization must not be null");
        this.authorizationEntityRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "an id must not be empty");
        return this.authorizationEntityRepository.findById(id)
                .map(authorizationEntityMapper::toObject)
                .orElse(null);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "a token must not be empty");

        Optional<OAuth2AuthorizationEntity> result;
        if (tokenType == null) {
            result = this.authorizationEntityRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(token);
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

        return result.map(authorizationEntityMapper::toObject)
                .orElse(null);
    }
}
