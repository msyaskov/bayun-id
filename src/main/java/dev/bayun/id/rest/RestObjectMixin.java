package dev.bayun.id.rest;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * @author Максим Яськов
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = IdRestObject.class, name = "id"),
        @JsonSubTypes.Type(value = UserRestObject.class, name = "user")
})
public abstract class RestObjectMixin {
}
