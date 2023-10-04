package dev.bayun.sso.validation.validator;

import dev.bayun.sso.validation.annotation.Firstname;

/**
 * Реализация {@link AbstractRegexpValidator} для проверки объектов аннотированных {@link Firstname}.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */

public class FirstnameValidator extends AbstractRegexpValidator<Firstname> {

	@Override
	public void init(Firstname annotation) {
		setRequired(annotation.required());
		setPredicate(annotation.regexp());
	}

}
