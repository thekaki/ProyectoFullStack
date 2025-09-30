package com.scabrera.cursospring.security;

import com.scabrera.cursospring.models.Permiso;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationService {

    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;

    public AuthorizationService(CurrentUserService currentUserService, UsuarioService usuarioService) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
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
