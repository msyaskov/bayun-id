package dev.bayun.sso.account;

import dev.bayun.sso.account.repository.AccountRepository;
import dev.bayun.sso.security.OAuth2Provider;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultAccountService implements AccountService {

    private AccountRepository accountRepository;

    @Override
    public AccountEntity save(OAuth2User oAuth2User, OAuth2Provider provider) {
        return switch (provider) {
            case GOOGLE -> saveFromGoogle(oAuth2User);
            case YANDEX -> saveFromYandex(oAuth2User);
        };
    }

    private AccountEntity saveFromGoogle(OAuth2User oAuth2User) {
        AccountEntity entity = new AccountEntity();
        entity.setEmail(oAuth2User.getAttribute("email"));
        entity.setFirstName(oAuth2User.getAttribute("given_name"));
        entity.setLastName(oAuth2User.getAttribute("family_name"));
        entity.setPicture(oAuth2User.getAttribute("picture"));

        return accountRepository.save(entity);
    }

    private AccountEntity saveFromYandex(OAuth2User oAuth2User) {
        AccountEntity entity = new AccountEntity();
        entity.setEmail(oAuth2User.getAttribute("default_email"));
        entity.setFirstName(oAuth2User.getAttribute("first_name"));
        entity.setLastName(oAuth2User.getAttribute("last_name"));
        entity.setPicture(getYandexPictureUrl(oAuth2User.getAttribute("default_avatar_id")));

        return accountRepository.save(entity);
    }

    private String getYandexPictureUrl(String pictureId) {
        return String.format("https://avatars.yandex.net/get-yapic/%s/%s", pictureId, "islands-200");
    }

}
