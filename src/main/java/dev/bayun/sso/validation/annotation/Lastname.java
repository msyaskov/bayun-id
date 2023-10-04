package dev.bayun.sso.validation.annotation;

import dev.bayun.sso.validation.validator.LastnameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация ограничения проверки фамилии пользователя.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Validation(validators = LastnameValidator.class)
public @interface Lastname {

	String DEFAULT_REGEXP = "^(?=.{1,30}$)(?!.*[ \\-.',]{2})[a-zA-Z]+([a-zA-Z \\-.',]*[a-zA-Z]+)*$";

	boolean required() default true;

	String regexp() default DEFAULT_REGEXP;

}
