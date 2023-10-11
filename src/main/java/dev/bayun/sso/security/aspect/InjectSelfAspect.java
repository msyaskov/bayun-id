package dev.bayun.sso.security.aspect;

import dev.bayun.sso.account.AccountService;
import dev.bayun.sso.account.entity.AccountEntity;
import dev.bayun.api.sso.AuthenticatedPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

/**
 * @author Максим Яськов
 */

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InjectSelfAspect {

    private final AccountService accountService;

    @Around("@annotation(InjectSelf)")
    public Object injectAccount(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();

        Integer selfParameterIndex = getSelfParameterIndex(parameters);
        if (selfParameterIndex == null) {
            return jp.proceed();
        }

        AccountEntity self = obtainAccountFromSecurityContextHolder();

        Object[] args = jp.getArgs();
        args[selfParameterIndex] = self;

        return jp.proceed(args);
    }

    private AccountEntity obtainAccountFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof AuthenticatedPrincipal principal) {
            return accountService.getById(principal.getId());
        } else {
            return null;
        }
    }

    private Integer getSelfParameterIndex(Parameter[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            Self selfAnnotation = parameters[i].getAnnotation(Self.class);
            if (selfAnnotation == null) {
                continue;
            }

            return i;
        }

        return null;
    }
}
