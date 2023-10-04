package dev.bayun.sso.validation.validator;

import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;

/**
 * Интерфейс для проверки аннотированных аннотацией ограничения проверки параметров.
 *
 * @param <A> тип аннотации ограничения проверки.
 * @param <T> тип параметра.
 * @author Maksim Yaskov
 * @since 0.0.4
 */
public interface Validator<A extends Annotation, T> {

	/**
	 * Инициализация реализующего класса перед проверкой.
	 *
	 * @param a аннотация.
	 */
	default void init(A a) {

	}

	/**
	 * Проверка значения.
	 *
	 * @param t проверяемый объект.
	 * @return <b>true</b> если объект прошел проверку, <b>false</b> если проверка не пройдена.
	 */
	boolean isValid(@Nullable T t);

}
