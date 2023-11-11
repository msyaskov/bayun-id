package dev.bayun.id.principal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
//@JsonDeserialize(using = OwnPrincipalDeserializer.class)
public abstract class OwnPrincipalMixin {


    public static OwnPrincipal fromJson(
            String password, Collection<SimpleGrantedAuthority> authorities, Map<String, Object> attributes) {
        return new OwnPrincipal(password, authorities, attributes);
    }

}