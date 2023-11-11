package dev.bayun.id.mvc;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Максим Яськов
 */

@Getter
@ToString
public enum Error {
    ACCESS_DENIED(403, "Access denied."),
    AUTH_RESTART(403, "Restart the authorization process."),
    EMAIL_UNOCCUPIED(400, "The email is not yet being used."),
    EMAIL_OCCUPIED(403, "The email is already in use."),
    INTERNAL(500, "Internal server error."),
    INVALID_REQUEST(400, "The request is not valid."),
    INVALID_REQUEST_PARAM(400, "One of the parameters specified is not valid."),
    MISSING_REQUEST_PARAM(400, "One of the parameters specified was missing."),
    NOT_FOUND(404, "The requested resource is not found."),
    USERNAME_OCCUPIED(400, "The username is already in use."),
    USERNAME_UNOCCUPIED(400, "The username is not yet being used.");

    private final int status;

    private final String description;

    Error(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getType() {
        return this.name();
    }

}
