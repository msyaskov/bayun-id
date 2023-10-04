package dev.bayun.sso.core.mapper;

public interface EntityMapper<E,O> {

    E toEntity(O object);

    O toObject(E entity);

}
