package dev.bayun.id.util;

import dev.bayun.id.principal.OwnPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Максим Яськов
 */
public class SecurityUtils {

    public static OwnPrincipal getPrincipal() {
        return (OwnPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
