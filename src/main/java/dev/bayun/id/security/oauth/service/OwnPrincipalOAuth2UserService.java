package dev.bayun.id.security.oauth.service;

import dev.bayun.id.account.AccountService;
import dev.bayun.id.account.Account;
import dev.bayun.id.principal.OwnPrincipal;
import dev.bayun.id.security.oauth.OAuth2Provider;
import dev.bayun.id.user.User;
import dev.bayun.id.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Максим Яськов
 */

@Service
@RequiredArgsConstructor
public class OwnPrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountService accountService;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Provider provider = OAuth2Provider.getByName(userRequest.getClientRegistration().getRegistrationId());

        String email = obtainEmail(oAuth2User, provider);
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Retrieved email must not be null or blank");
        }

        // orElse -> register new User
        UUID userId = userService.getUserIdByEmail(email)
                .orElseGet(() -> userService.register(obtainUserByProvider(oAuth2User, provider)));

        Account accountById = accountService.findById(userId).orElseGet(() -> accountService.register(Account.builder()
                        .id(userId)
                .build()));

        return OwnPrincipal.builder()
                .sub(accountById.getId().toString())
                .password(accountById.getPasswordHash())
                .locked(accountById.isLocked())
                .enabled(accountById.isEnabled())
                .attribute("from_provider", oAuth2User.getAttributes())
                .authorities(authorities -> {
                    for (String authority : accountById.getAuthorities()) {
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }
                })
                .build();
    }

    protected String obtainEmail(OAuth2User oAuth2User, OAuth2Provider provider) {
        return switch (provider) {
            case GOOGLE -> oAuth2User.getAttribute("email");
            case YANDEX -> oAuth2User.getAttribute("default_email");
        };
    }

    protected User obtainUserByProvider(OAuth2User oAuth2User, OAuth2Provider provider) {
        User candidate = switch (provider) {
            case GOOGLE -> obtainUserFromGoogle(oAuth2User);
            case YANDEX -> obtainUserFromYandex(oAuth2User);
        };
        candidate.setEmail(obtainEmail(oAuth2User, provider));
        candidate.setNickname(UUID.randomUUID().toString());

        return candidate;
    }

    protected User obtainUserFromGoogle(OAuth2User oAuth2User) {
        return User.builder()
                .givenName(Objects.requireNonNullElse(oAuth2User.getAttribute("given_name"), ""))
                .familyName(Objects.requireNonNullElse(oAuth2User.getAttribute("family_name"), ""))
                .picture(oAuth2User.getAttribute("picture"))
                .build();
    }

    protected User obtainUserFromYandex(OAuth2User oAuth2User) {
        return User.builder()
                .givenName(Objects.requireNonNullElse(oAuth2User.getAttribute("first_name"), ""))
                .familyName(Objects.requireNonNullElse(oAuth2User.getAttribute("last_name"), ""))
                .picture(String.format("https://avatars.yandex.net/get-yapic/%s/islands-200", Objects.requireNonNullElse(oAuth2User.getAttribute("default_avatar_id"), "")))
                .build();
    }
}
