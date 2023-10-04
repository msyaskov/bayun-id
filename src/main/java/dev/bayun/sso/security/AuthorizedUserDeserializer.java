package dev.bayun.sso.security;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Максим Яськов
 */

@RequiredArgsConstructor
public class AuthorizedUserDeserializer extends JsonDeserializer<AuthorizedUser> {

    @Override
    public AuthorizedUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        String username = readJsonNode(jsonNode, "username").asText();
        String password = readJsonNode(jsonNode, "password").asText("");
        boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean(true);
        boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean(true);
        boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean(true);
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean(true);
        return new AuthorizedUser(UUID.fromString(username), password, Collections.emptySet(),
                accountNonExpired, credentialsNonExpired, accountNonLocked, enabled);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
