package com.scabrera.cursospring.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final AuthorizationService authorizationService;

    public CustomPermissionEvaluator(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (permission == null) return false;
        String permisoName = permission.toString();
        
        if (targetDomainObject instanceof Serializable serializableObject) {
            String targetType = targetDomainObject.getClass().getSimpleName();
            return authorizationService.hasPermission(authentication, serializableObject, targetType, permisoName);
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (permission == null) return false;
        String permisoName = permission.toString();
        return authorizationService.hasPermission(authentication, targetId, targetType, permisoName);
    }
}