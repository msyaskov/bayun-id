package dev.bayun.sso.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Максим Яськов
 */

@Getter
@Setter
@Builder
public class AccountDto {

    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String pictureUrl;

}
