package dev.bayun.sso.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String picture; // url
    private Boolean active;

}
