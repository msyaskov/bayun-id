package dev.bayun.sso.validation.annotation;

import dev.bayun.sso.validation.validator.FirstnameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация ограничения проверки имени пользователя.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Validation(validators = FirstnameValidator.class)
public @interface Firstname {

	String DEFAULT_REGEXP = "^(?=.{1,30}$)(?!.*[ \\-.',]{2})[a-zA-Z]+([a-zA-Z \\-.',]*[a-zA-Z]+)*$";

	boolean required() default true;

	String regexp() default DEFAULT_REGEXP;

}
