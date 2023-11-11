package dev.bayun.id.security.oauth.service;

import dev.bayun.id.account.AccountService;
import dev.bayun.id.user.User;
import dev.bayun.id.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Максим Яськов
 */
@Service
@RequiredArgsConstructor
public class OidcUserInfoService {

    private final AccountService accountService;

    private final UserService userService;

    public OidcUserInfo loadUser(UUID accountId) {
        User userById = userService.getUserById(accountId).orElseThrow();
        return convertToUserInfo(userById);
    }

    public OidcUserInfo convertToUserInfo(User user) {
        return OidcUserInfo.builder()
                .subject(user.getId().toString())
                .nickname(user.getNickname())
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .build();
    }
}
