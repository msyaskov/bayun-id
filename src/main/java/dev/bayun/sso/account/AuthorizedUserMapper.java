package dev.bayun.sso.account;

import java.util.Collections;

public class AuthorizedUserMapper {

    public static AuthorizedUser map(AccountEntity entity) {
        return new AuthorizedUser(entity.getId(), entity.getEmail(), Collections.emptySet());
    }

}
