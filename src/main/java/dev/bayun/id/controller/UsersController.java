package dev.bayun.id.controller;

import dev.bayun.sdk.rest.core.RestDocument;
import dev.bayun.id.rest.UserRestObject;
import dev.bayun.id.user.User;
import dev.bayun.id.user.UserService;
import dev.bayun.id.util.SecurityUtils;
import dev.bayun.id.validation.annotation.GivenName;
import dev.bayun.id.validation.annotation.FamilyName;
import dev.bayun.id.validation.annotation.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestDocument getUserById(@PathVariable String userId) {
        User userById = userService.getUserById(convertStringToUUID(userId)).orElseThrow();
        return RestDocument.builder()
                .object("user", new UserRestObject(userById))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestDocument getUserMe() {
        User userForMe = userService.getUserById(convertStringToUUID(SecurityUtils.getPrincipal().getUsername())).orElseThrow();
        return RestDocument.builder()
                .object("user", new UserRestObject(userForMe))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void patchUserMe(@Validated @Nickname @RequestParam(required = false) String nickname,
                                @Validated @GivenName @RequestParam(required = false) String givenName,
                                @Validated @FamilyName @RequestParam(required = false) String familyName) {
        userService.update(convertStringToUUID(SecurityUtils.getPrincipal().getUsername()),
                nickname, givenName, familyName);
    }

    private UUID convertStringToUUID(String s) {
        try {
            return UUID.fromString(s);
        } catch (Exception e) {
            throw new RuntimeException("%s is not a UUID".formatted(s));
        }
    }

}
