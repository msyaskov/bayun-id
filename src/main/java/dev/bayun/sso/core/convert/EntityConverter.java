package dev.bayun.sso.core.convert;

public interface EntityConverter<E,O> {

    E convertToEntity(O object);

    O convertToObject(E entity);

}
