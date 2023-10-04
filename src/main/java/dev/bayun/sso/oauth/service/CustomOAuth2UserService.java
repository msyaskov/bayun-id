package dev.bayun.sso.oauth.service;

import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.sso.account.repository.AccountRepository;
import dev.bayun.sso.oauth.converter.AccountEntityToAuthorizedUserConverter;
import dev.bayun.sso.oauth.OAuth2Provider;
import dev.bayun.sso.security.AuthorizedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Максим Яськов
 */

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    private final Converter<AccountEntity, AuthorizedUser> accountEntityToAuthorizedUserConverter;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Provider provider = OAuth2Provider.getByName(userRequest.getClientRegistration().getRegistrationId());

        String email = obtainEmail(oAuth2User, provider);
        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseGet(() -> register(oAuth2User, provider));

        return accountEntityToAuthorizedUserConverter.convert(accountEntity);
    }

    private AccountEntity register(OAuth2User oAuth2User, OAuth2Provider provider) {
        return accountRepository.save(obtainAccount(oAuth2User, provider));
    }

    protected String obtainEmail(OAuth2User oAuth2User, OAuth2Provider provider) {
        return switch (provider) {
            case GOOGLE -> oAuth2User.getAttribute("email");
            case YANDEX -> oAuth2User.getAttribute("default_email");
        };
    }

    protected AccountEntity obtainAccount(OAuth2User oAuth2User, OAuth2Provider provider) {
        AccountEntity accountEntity = switch (provider) {
            case GOOGLE -> obtainAccountFromGoogle(oAuth2User);
            case YANDEX -> obtainAccountFromYandex(oAuth2User);
        };

        accountEntity.setAuthorities(Collections.emptySet());
        accountEntity.setCreationDate(LocalDateTime.now());
        accountEntity.setLastUpdateDate(LocalDateTime.now());
        accountEntity.setCredentialExpired(false);
        accountEntity.setEnabled(true);
        accountEntity.setExpired(false);
        accountEntity.setLocked(false);
        accountEntity.setPasswordHash("");
        accountEntity.setUsername(generateUsername());
        accountEntity.setUsing2FA(false);

        return accountEntity;
    }

    protected AccountEntity obtainAccountFromGoogle(OAuth2User oAuth2User) {
        AccountEntity entity = new AccountEntity();
        entity.setEmail(oAuth2User.getAttribute("email"));
        entity.setFirstName(oAuth2User.getAttribute("given_name"));
        entity.setLastName(oAuth2User.getAttribute("family_name"));
        entity.setPictureUrl(oAuth2User.getAttribute("picture"));

        return entity;
    }

    protected AccountEntity obtainAccountFromYandex(OAuth2User oAuth2User) {
        AccountEntity entity = new AccountEntity();
        entity.setEmail(oAuth2User.getAttribute("default_email"));
        entity.setFirstName(oAuth2User.getAttribute("first_name"));
        entity.setLastName(oAuth2User.getAttribute("last_name"));
        entity.setPictureUrl(String.format("https://avatars.yandex.net/get-yapic/%s/islands-200", oAuth2User.getAttribute("default_avatar_id")));

        return entity;
    }

    protected String generateUsername() {
        return "u"+UUID.randomUUID();
    }

}
