package dev.bayun.id.principal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;

/**
 * @author Максим Яськов
 */

public class OwnPrincipalDeserializer extends JsonDeserializer<OwnPrincipal> {

    @Override
    public OwnPrincipal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        return OwnPrincipal.builder()
                .password(readJsonNode(jsonNode, "password").asText(""))
                .authorities(authorities -> {
                    JsonNode node = readJsonNode(jsonNode, "authorities");
                    if (!node.isArray()) {
                        return;
                    }

                    node.forEach(entry -> {
                        try {
                            authorities.add(new SimpleGrantedAuthority(entry.asText()));
                        } catch (Exception ignored) {
                            // skip entry
                        }
                    });
                })
                .attributes(attributes -> {
                    JsonNode node = readJsonNode(jsonNode, "attributes");
                    if (!node.isArray()) {
                        return;
                    }

                    node.forEach(entry -> {
                        try {
                            JsonNode keyNode = entry.get(0);
                            JsonNode valueNode = entry.get(1);
                            if (keyNode.equals(NullNode.getInstance()) || valueNode.equals(NullNode.getInstance())) {
                                return;
                            }

                            attributes.put(keyNode.asText(), valueNode.asText());
                        } catch (Exception ignored) {
                            // skip entry
                        }
                    });
                })
                .build();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
