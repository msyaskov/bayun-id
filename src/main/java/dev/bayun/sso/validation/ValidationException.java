package dev.bayun.sso.validation;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;

/**
 * Выбрасывается, когда имеются ошибки проверки.
 *
 * @author Maksim Yaskov
 * @since 0.0.4
 */
public class ValidationException extends RuntimeException {

	@Getter
	private final Set<String> validationResult;

	public ValidationException(Set<String> validationResult) {
		this(validationResult, null, null);
	}

	public ValidationException(Set<String> validationResult, String message) {
		this(validationResult, message, null);
	}

	public ValidationException(Set<String> validationResult, Throwable cause) {
		this(validationResult, null, cause);
	}

	public ValidationException(Set<String> validationResult, String message, Throwable cause) {
		super(message, cause);
		Assert.notNull(validationResult, "a validationResult must not be null");
		this.validationResult = Collections.unmodifiableSet(validationResult);
	}

}
