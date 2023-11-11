package dev.bayun.id.security.oauth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PrincipalDto {

    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String pictureUrl;

}
