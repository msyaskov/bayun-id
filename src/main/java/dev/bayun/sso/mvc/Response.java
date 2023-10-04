package dev.bayun.sso.mvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Максим Яськов
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private boolean ok;

    private T result;

    public Response() {
        this(true, null);
    }

    public Response(T result) {
        this(true, result);
    }

    public Response(boolean ok, T result) {
        this.ok = ok;
        this.result = result;
    }

}
