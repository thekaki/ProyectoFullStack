package com.scabrera.cursospring.security;

import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.ArticuloRepository;
import com.scabrera.cursospring.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;
    private final ArticuloRepository articuloRepository; // 👈 añadir repo si necesitas más entidades

    public AuthorizationService(CurrentUserService currentUserService,
                                UsuarioService usuarioService,
                                ArticuloRepository articuloRepository) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
        this.articuloRepository = articuloRepository;
    }

    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 String targetType,
                                 String permisoName) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioService.buscarUsuarioEntity(currentUserId);

        System.out.println("entidad " + targetDomainObject);
        System.out.println("permiso input " + permisoName);

        // 1️⃣ Caso: targetDomainObject es entidad cargada
        if (targetDomainObject instanceof Ownable ownable) {
            Long ownerId = ownable.getOwnerId();
            System.out.println("current user id: " + currentUserId);
            System.out.println("ownerId " + ownerId);
            if (ownerId != null && ownerId.equals(currentUserId)) {
                return true;
            }
        }

        // 2️⃣ Caso: solo llega el ID + tipo (ejemplo: eliminarArticulo)
        if (targetDomainObject instanceof Long id && targetType != null) {
            if ("Articulo".equalsIgnoreCase(targetType)) {
                return articuloRepository.findById(id)
                        .map(articulo -> {
                            Long ownerId = articulo.getOwnerId();
                            System.out.println("ownerId " + ownerId);
                            return ownerId != null && ownerId.equals(currentUserId);
                        })
                        .orElse(false);
            }
            // 🔮 aquí podrías añadir otros entityType en el futuro
        }

        // 3️⃣ Validar permisos por roles
        if (permisoName != null && user.getRoles() != null) {
            System.out.println("roles " + user.getRoles());
            boolean tienePermiso = user.getRoles().stream()
                    .flatMap(rol -> rol.getPermisos().stream())
                    .anyMatch(p -> permisoName.equalsIgnoreCase(p.getNombre()));
            System.out.println("tiene permiso " + tienePermiso);
            if (tienePermiso) return true;
        }

        // 4️⃣ Denegado por defecto
        return false;
    }
}

