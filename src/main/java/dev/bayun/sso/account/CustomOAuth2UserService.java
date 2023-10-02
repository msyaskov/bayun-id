package dev.bayun.sso.account;

import dev.bayun.sso.security.OAuth2Provider;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private AccountService accountService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);                            // Загружаем пользователя, как это было до
        String clientRegId = userRequest.getClientRegistration().getRegistrationId();   // Получаем наименование провайдера (google, github и т.д.)
        OAuth2Provider provider = OAuth2Provider.getByName(clientRegId);                   // Для удобства создадим enum AuthProvider и по наименованию провайдера получим значение
        return AuthorizedUserMapper.map(accountService.save(oAuth2User, provider));
    }
}
