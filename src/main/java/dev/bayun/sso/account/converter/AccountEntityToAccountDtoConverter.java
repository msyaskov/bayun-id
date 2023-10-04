package dev.bayun.sso.account.converter;

import dev.bayun.sso.account.dto.AccountDto;
import dev.bayun.sso.account.entity.AccountEntity;
import jakarta.persistence.Entity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Максим Яськов
 */

@Component
public class AccountEntityToAccountDtoConverter implements Converter<AccountEntity, AccountDto> {

    @Override
    public AccountDto convert(@NonNull AccountEntity source) {
        return AccountDto.builder()
                .id(source.getId())
                .email(source.getEmail())
                .username(source.getUsername())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .pictureUrl(source.getPictureUrl())
                .build();
    }
}
