package dev.bayun.id.validation.annotation;

import dev.bayun.id.validation.validator.FamilyNameValidator;

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
@Validation(validators = FamilyNameValidator.class)
public @interface FamilyName {

	String DEFAULT_REGEXP = "^(?=.{1,30}$)(?!.*[ \\-.',]{2})[a-zа-яёЁA-ZА-Я]+([a-zа-яёЁA-ZА-Я \\-.',]*[a-zа-яёЁA-ZА-Я]+)*$";

	boolean required() default true;

	String regexp() default DEFAULT_REGEXP;

}
