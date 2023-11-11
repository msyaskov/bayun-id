package dev.bayun.id.rest;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.bayun.sdk.rest.core.RestObject;
import dev.bayun.id.user.User;

/**
 * @author Максим Яськов
 */
@JsonTypeName("user")
public class UserRestObject extends RestObject<User, Void> {

    public UserRestObject() {
        super();
    }

    public UserRestObject(User object) {
        super(object, null);
    }
}
