package dev.bayun.sso.oauth.converter;

import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.sso.oauth.dto.PrincipalDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityToPrincipalDtoConverter implements Converter<AccountEntity, PrincipalDto> {

    @Override
    public PrincipalDto convert(@NonNull AccountEntity source) {
        return PrincipalDto.builder()
                .id(source.getId())
                .email(source.getEmail())
                .username(source.getUsername())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pictureUrl(source.getPictureUrl())
                .build();
    }

}
