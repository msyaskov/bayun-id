package dev.bayun.id.validation.validator;

import dev.bayun.id.validation.annotation.Nickname;

/**
 * Реализация {@link AbstractRegexpValidator} для проверки объектов аннотированных {@link Nickname}.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
public class NicknameValidator extends AbstractRegexpValidator<Nickname> {

	@Override
	public void init(Nickname annotation) {
		setRequired(annotation.required());
		setPredicate(annotation.regexp());
	}

}