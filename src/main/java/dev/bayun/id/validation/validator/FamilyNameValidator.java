package dev.bayun.id.validation.validator;

import dev.bayun.id.validation.annotation.FamilyName;

/**
 * Реализация {@link AbstractRegexpValidator} для проверки объектов аннотированных {@link FamilyName}.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
public class FamilyNameValidator extends AbstractRegexpValidator<FamilyName> {

	@Override
	public void init(FamilyName annotation) {
		setRequired(annotation.required());
		setPredicate(annotation.regexp());
	}

}
