package dev.bayun.sso.validation.validator;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Абстрактный класс реализующий {@link Validator}.
 * <p>Используется для валидация параметров типа {@code String} на основе регулярного выражения.</p>
 * <p>Для использования необходимо предоставить реализацию {@link Validator#init(Annotation)},
 *      в которой установить значения {@code required} и {@code predicate}.</p>
 * @param <A> тип аннотации.
 * @since 0.0.4
 * @author Maksim Yaskov
 */
public abstract class AbstractRegexpValidator<A extends Annotation> implements Validator<A, String> {

    protected boolean required;

    protected Predicate<String> predicate;

    @Override
    public boolean isValid(String s) {
        if (s == null) {
            return !required;
        }

        return predicate.test(s);
    }

    protected void setPredicate(String regexp) {
        this.predicate = Pattern.compile(regexp).asPredicate();
    }

    protected void setRequired(boolean required) {
        this.required = required;
    }
}
