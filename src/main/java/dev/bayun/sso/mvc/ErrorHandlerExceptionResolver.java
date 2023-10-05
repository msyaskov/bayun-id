package dev.bayun.sso.mvc;

import dev.bayun.sso.validation.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Максим Яськов
 */

@Slf4j
@Component
public class ErrorHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

    @Getter
    @Setter
    private int order;

    public ErrorHandlerExceptionResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE+10);
    }

    @Override
    public ModelAndView resolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                         Object handler, @NonNull Exception ex) {

        ModelAndView mav;

        if (ex instanceof ValidationException validationException) {
            mav = handleValidationException(validationException, request, response);
        } else if (ex instanceof MissingServletRequestParameterException missingServletRequestParameterException) {
            mav = createResponse(Error.MISSING_REQUEST_PARAM, Map.of("parameters", new String[]{missingServletRequestParameterException.getParameterName()}), request, response);
        } else if (ex instanceof MissingServletRequestPartException missingServletRequestPartException) {
            mav = createResponse(Error.MISSING_REQUEST_PARAM, Map.of("parameters", new String[]{missingServletRequestPartException.getRequestPartName()}), request, response);
        } else {
            mav = handleException(ex, request, response);
        }

        return mav;
    }

    protected ModelAndView handleValidationException(ValidationException bindException, HttpServletRequest request, HttpServletResponse response) {
        String[] parameters = bindException.getValidationResult().toArray(String[]::new);
        return createResponse(Error.INVALID_REQUEST_PARAM, Map.of("parameters", parameters), request, response);
    }

    protected ModelAndView handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved Exception", e);
        return createResponse(Error.INTERNAL, null, request, response);
    }

    protected ModelAndView createResponse(Error error, Map<String,Object> properties, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(error.getStatus());

        ModelAndView mav;
        if (isHtmlAcceptable(request)) {
            mav = new ModelAndView("error.html");
        } else {
            mav = new ModelAndView(new MappingJackson2JsonView());
        }

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", System.currentTimeMillis());
        errorMap.put("status", error.getStatus());
        errorMap.put("type", error.getType());
        errorMap.put("description", error.getDescription());
        errorMap.putAll(Objects.requireNonNullElse(properties, Map.of()));

        mav.addObject("ok", false);
        mav.addObject("error", errorMap);

        return mav;
    }

    protected boolean isHtmlAcceptable(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/api")) {
            return false;
        }

        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if (accept == null) {
            return false;
        }

        try {
            return MediaType.parseMediaTypes(accept).stream().anyMatch(MediaType.TEXT_HTML::isCompatibleWith);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}