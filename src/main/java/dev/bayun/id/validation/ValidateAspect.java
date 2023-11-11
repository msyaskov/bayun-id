package dev.bayun.id.validation;

import dev.bayun.id.validation.annotation.Validation;
import dev.bayun.id.validation.validator.Validator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class ValidateAspect {

	@SuppressWarnings("unchecked")
	@Before("@annotation(Validate)")
	public void validate(JoinPoint jp) {
		Set<String> validationResult = new HashSet<>();

		Method method = ((MethodSignature) jp.getSignature()).getMethod();
		Parameter[] params = method.getParameters();
		Object[] paramValues = jp.getArgs();
		for (int i = 0; i < params.length; i++) {
			Parameter param = params[i];
			Object paramValue = paramValues[i];
			if (!param.isAnnotationPresent(Validated.class)) {
				continue;
			}

			Arrays.stream(param.getAnnotations())
					.filter(annotation -> annotation.annotationType().getAnnotation(Validation.class) != null)
					.flatMap(annotation -> {
						Validation validation = annotation.annotationType().getAnnotation(Validation.class);
						Class<? extends Validator<?, ?>>[] validatorClasses = validation.validators();
						return Arrays.stream(validatorClasses).map(validatorClass -> {
							try {
								Validator<Annotation, Object> validator = (Validator<Annotation, Object>) validatorClass.getConstructor()
										.newInstance();
								validator.init(annotation);
								return validator;
							} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
								throw new RuntimeException(e);
							}
						});
					})
					.forEach(validator -> {
						if (!validator.isValid(paramValue)) {
							validationResult.add(param.getName());
						}
					});
		}

		if (!validationResult.isEmpty()) {
			throw new ValidationException(validationResult);
		}
	}

}
