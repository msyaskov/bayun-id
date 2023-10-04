package dev.bayun.sso.validation.validator;

import dev.bayun.sso.validation.annotation.Username;

/**
 * Реализация {@link AbstractRegexpValidator} для проверки объектов аннотированных {@link Username}.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
public class UsernameValidator extends AbstractRegexpValidator<Username> {

	@Override
	public void init(Username annotation) {
		setRequired(annotation.required());
		setPredicate(annotation.regexp());
	}

}