package dev.bayun.sso.validation.validator;

import dev.bayun.sso.validation.annotation.Lastname;

/**
 * Реализация {@link AbstractRegexpValidator} для проверки объектов аннотированных {@link Lastname}.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
public class LastnameValidator extends AbstractRegexpValidator<Lastname> {

	@Override
	public void init(Lastname annotation) {
		setRequired(annotation.required());
		setPredicate(annotation.regexp());
	}

}
