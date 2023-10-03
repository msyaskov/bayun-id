package dev.bayun.sso.util.converter;

import dev.bayun.sso.security.Authorities;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class AuthoritiesConverter implements AttributeConverter<Authorities, String> {

    private static final String DELIMITER = ", ";

    @Override
    public String convertToDatabaseColumn(Authorities authorities) {
        if (authorities == null) {
            return null;
        }

        if (authorities.getAuthorities().isEmpty()) {
            return "";
        }

        return StringUtils.join(authorities.getAuthorities(), DELIMITER);
    }

    @Override
    public Authorities convertToEntityAttribute(String attr) {
        if (attr == null || attr.isEmpty()) {
            return new Authorities(new HashSet<>());
        }

        Set<? extends GrantedAuthority> authorities = Arrays.stream(attr.split(DELIMITER))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return new Authorities(new HashSet<>(authorities));
    }
}
