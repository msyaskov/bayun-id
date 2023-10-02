package dev.bayun.sso.account;

import dev.bayun.sso.security.OAuth2Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AccountService {

    AccountEntity save(OAuth2User oAuth2User, OAuth2Provider provider);

}
