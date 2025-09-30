package com.scabrera.cursospring.security;

import com.scabrera.cursospring.models.Permiso;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class AuthorizationService {

    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;

    public AuthorizationService(CurrentUserService currentUserService, UsuarioService usuarioService) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
    }

    public boolean hasPermission(Authentication authentication, Serializable targetDomainObject, String targetType, String permisoName) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioService.buscarUsuarioEntity(currentUserId);

        // 1. Si el objeto soporta ownership -> validamos propietario
        if (targetDomainObject instanceof Ownable ownable) {
            Long ownerId = ownable.getOwnerId();
            if (ownerId != null && ownerId.equals(currentUserId)) {
                return true;
            }
        }

        // 2. Validar permisos por roles
        if (permisoName != null) {
            boolean tienePermiso = user.getRoles().stream()
                    .flatMap(r -> r.getPermisos().stream())
                    .anyMatch(p -> permisoName.equals(p.getNombre()));

            if (tienePermiso) return true;
        }

        return false; // 3. default deny
    }

    public void checkOwnershipOrPermission(Long ownerId, List<Permiso> listaPermiso) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioService.buscarUsuarioEntity(currentUserId);

        boolean tienePermiso = user.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .anyMatch(listaPermiso::contains);

        if (!ownerId.equals(currentUserId) && !tienePermiso) {
            throw new RuntimeException("No tienes permisos para este recurso");
        }
    }

    public void checkPermission(List<Permiso> listaPermiso) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioService.buscarUsuarioEntity(currentUserId);

        boolean tienePermiso = user.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .anyMatch(listaPermiso::contains);

        if (!tienePermiso) {
            throw new RuntimeException("No tienes permisos para este recurso");
        }
    }
}