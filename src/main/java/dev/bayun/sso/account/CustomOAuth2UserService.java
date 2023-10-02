package dev.bayun.sso.account;

import dev.bayun.sso.account.repository.AccountRepository;
import dev.bayun.sso.security.OAuth2Provider;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Provider provider = OAuth2Provider.getByName(userRequest.getClientRegistration().getRegistrationId());

        String email = obtainEmail(oAuth2User, provider);
        Account account = accountRepository.getByEmail(email);
        if (account == null) {
            account = obtainAccount(oAuth2User, provider);
            account = accountRepository.save(account);
        }

        return AuthorizedUserMapper.map(account);
    }

    protected String obtainEmail(OAuth2User oAuth2User, OAuth2Provider provider) {
        return switch (provider) {
            case GOOGLE -> oAuth2User.getAttribute("email");
            case YANDEX -> oAuth2User.getAttribute("default_email");
        };
    }

    protected Account obtainAccount(OAuth2User oAuth2User, OAuth2Provider provider) {
        Account account = switch (provider) {
            case GOOGLE -> obtainAccountFromGoogle(oAuth2User);
            case YANDEX -> obtainAccountFromYandex(oAuth2User);
        };

        account.setAuthorities(Collections.emptySet());
        account.setCreationDate(LocalDateTime.now());
        account.setCredentialExpired(false);
        account.setEnabled(true);
        account.setExpired(false);
        account.setLocked(false);
        account.setPassword("");
        account.setUsername(generateUsername());
        account.setUsing2FA(false);

        return account;
    }

    protected Account obtainAccountFromGoogle(OAuth2User oAuth2User) {
        Account entity = new Account();
        entity.setEmail(oAuth2User.getAttribute("email"));
        entity.setFirstName(oAuth2User.getAttribute("given_name"));
        entity.setLastName(oAuth2User.getAttribute("family_name"));
        entity.setPictureUrl(oAuth2User.getAttribute("picture"));

        return entity;
    }

    protected Account obtainAccountFromYandex(OAuth2User oAuth2User) {
        Account entity = new Account();
        entity.setEmail(oAuth2User.getAttribute("default_email"));
        entity.setFirstName(oAuth2User.getAttribute("first_name"));
        entity.setLastName(oAuth2User.getAttribute("last_name"));
        entity.setPictureUrl(String.format("https://avatars.yandex.net/get-yapic/%s/islands-200", oAuth2User.getAttribute("default_avatar_id").toString()));

        return entity;
    }

    protected String generateUsername() {
        return "u"+UUID.randomUUID();
    }

}
