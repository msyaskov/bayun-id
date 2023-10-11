package dev.bayun.sso.controller;

import dev.bayun.sso.account.AccountService;
import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.api.sso.UserInfo;
import dev.bayun.api.sso.http.UserInfoResultBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Максим Яськов
 */

@RestController
@RequiredArgsConstructor
public class UserInfoController {


    private final AccountService accountService;

    @GetMapping("/api/user-info/{userId}")
    public UserInfoResultBody getUserInfoById(@PathVariable("userId")UUID userId) {
        AccountEntity entity = accountService.getById(userId);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(entity.getId());
        userInfo.setEmail(entity.getEmail());
        userInfo.setUsername(entity.getUsername());
        userInfo.setFirstName(entity.getFirstName());
        userInfo.setLastName(entity.getLastName());
        userInfo.setPictureUrl(entity.getPictureUrl());

        return UserInfoResultBody.of(true, userInfo);
    }

}
