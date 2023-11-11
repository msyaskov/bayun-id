package dev.bayun.id.validation.validator;

import dev.bayun.id.validation.annotation.GivenName;

/**
 * Реализация {@link AbstractRegexpValidator} для проверки объектов аннотированных {@link GivenName}.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */

public class GivenNameValidator extends AbstractRegexpValidator<GivenName> {

	@Override
	public void init(GivenName annotation) {
		setRequired(annotation.required());
		setPredicate(annotation.regexp());
	}

}
