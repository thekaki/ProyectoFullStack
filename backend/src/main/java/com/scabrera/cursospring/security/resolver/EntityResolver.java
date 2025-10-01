package com.scabrera.cursospring.security.resolver;

public interface EntityResolver {

    Object resolve(Long id);

    String getEntityName();
}
