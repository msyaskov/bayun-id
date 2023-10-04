package dev.bayun.sso.controller;

import dev.bayun.sso.account.AccountService;
import dev.bayun.sso.account.dto.AccountDto;
import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.sso.mvc.Response;
import dev.bayun.sso.security.aspect.InjectSelf;
import dev.bayun.sso.security.aspect.Self;
import dev.bayun.sso.validation.Validate;
import dev.bayun.sso.validation.Validated;
import dev.bayun.sso.validation.annotation.Firstname;
import dev.bayun.sso.validation.annotation.Lastname;
import dev.bayun.sso.validation.annotation.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Максим Яськов
 */

@RestController
@RequiredArgsConstructor
public class SelfAccountController {

    public static final String SELF_PATH = "/api/self";

    private final Converter<AccountEntity, AccountDto> accountEntityToAccountDtoConverter;

    private final AccountService accountService;

    @InjectSelf
    @GetMapping(path = SELF_PATH)
    public Response<AccountDto> getSelf(@Self AccountEntity self) {
        return new Response<>(accountEntityToAccountDtoConverter.convert(self));
    }

    @Validate
    @InjectSelf
    @PatchMapping(path = SELF_PATH)
    public Response<AccountDto> patchSelf(@Self AccountEntity self,
                                @Validated @Username(required = false) @RequestParam(required = false) String username,
                                @Validated @Firstname(required = false) @RequestParam(required = false) String firstName,
                                @Validated @Lastname(required = false) @RequestParam(required = false) String lastName) {

        if (username != null) {
            self.setUsername(username);
        }

        if (firstName != null) {
            self.setFirstName(firstName);
        }

        if (lastName != null) {
            self.setLastName(lastName);
        }

        self = accountService.save(self);

        return new Response<>(accountEntityToAccountDtoConverter.convert(self));
    }
}
