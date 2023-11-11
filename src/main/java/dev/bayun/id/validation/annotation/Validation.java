package dev.bayun.id.validation.annotation;

import dev.bayun.id.validation.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает аннотацию как аннотацию ограничения проверки.
 * <p>Аннотация ограничения проверки должна быть аннотирована аннотацией {@link Validation}, которая ссылается на список реализаций проверки.</p>
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

	Class<? extends Validator<?, ?>>[] validators();

}
