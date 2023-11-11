package dev.bayun.id.validation.annotation;

import dev.bayun.id.validation.validator.NicknameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация ограничения проверки короткого имени пользователя.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Validation(validators = NicknameValidator.class)
public @interface Nickname {

	String DEFAULT_REGEXP = "^(?!.*_{2}.*)\\w{1,30}$";

	boolean required() default false;

	String regexp() default DEFAULT_REGEXP;

}