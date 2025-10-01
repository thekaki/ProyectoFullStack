package com.scabrera.cursospring.security;

import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.security.resolver.EntityResolver;
import com.scabrera.cursospring.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;
    private final Map<String, EntityResolver> resolvers;

    public AuthorizationService(CurrentUserService currentUserService,
                                UsuarioService usuarioService,
                                List<EntityResolver> resolverList) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
        this.resolvers = resolverList.stream()
                .collect(Collectors.toMap(EntityResolver::getEntityName, r -> r));
    }

    public boolean hasPermission(Authentication authentication, String permisoName) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioService.buscarUsuarioEntity(currentUserId);

        if (permisoName != null && user.getRoles() != null) {
            return user.getRoles().stream()
                    .flatMap(rol -> rol.getPermisos().stream())
                    .anyMatch(p -> permisoName.equalsIgnoreCase(p.getNombre()));
        }
        return false;
    }

    public boolean hasPermissionOverObject(Authentication authentication,
                                           Object targetDomainObject,
                                           String targetType,
                                           String permisoName) {
        Long currentUserId = currentUserService.getCurrentUserId();

        if (targetDomainObject instanceof Ownable ownable) {
            Long ownerId = ownable.getOwnerId();
            if (ownerId != null && ownerId.equals(currentUserId)) {
                return true;
            }
        }

        return hasPermission(authentication, permisoName);
    }

    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 String permisoName) {
        Object target = null;

        if (targetType != null && targetId != null && resolvers.containsKey(targetType)) {
            target = resolvers.get(targetType).resolve((Long) targetId);
        }

        return hasPermissionOverObject(authentication, target, targetType, permisoName);
    }
}
