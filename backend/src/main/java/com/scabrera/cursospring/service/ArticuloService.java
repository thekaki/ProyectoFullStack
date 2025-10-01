package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.Permiso;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.ArticuloRepository;
import com.scabrera.cursospring.security.AuthorizationService;
import com.scabrera.cursospring.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepo;
    private final UsuarioService usuarioService;
    private final CurrentUserService currentUserService;
    private final AuthorizationService authorizationService;
    private final PermisoService permisoService;

    public ArticuloService(ArticuloRepository articuloRepo, UsuarioService usuarioService, CurrentUserService currentUserService, AuthorizationService authorizationService, PermisoService permisoService) {
        this.articuloRepo = articuloRepo;
        this.usuarioService = usuarioService;
        this.currentUserService = currentUserService;
        this.authorizationService = authorizationService;
        this.permisoService = permisoService;
    }

    public List<ArticuloListResponseDTO> traerArticulos() {
        return articuloRepo.findAllWithoutContenido();
    }

    public Articulo buscarId(Long id) {
        return articuloRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Artículo no Encontrado"));
    }

    public Articulo crearArticulo(Articulo articulo) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioService.buscarUsuarioEntity(currentUserId);

        List<Permiso> listaPermiso = permisoService.buscarPermisoPorNombreEntidad("ARTICULO_CREATE");
        authorizationService.checkOwnershipOrPermission(currentUserId, listaPermiso);

        articulo.setPropietario(user);
        return articuloRepo.save(articulo);
    }

    public Articulo eliminarArticulo(Long id) {
        Articulo articulo = buscarId(id);
        List<Permiso> listaPermiso = permisoService.buscarPermisoPorNombreEntidad("ARTICULO_DELETE");

        authorizationService.checkOwnershipOrPermission(articulo.getPropietario().getId(), listaPermiso);

        articuloRepo.deleteById(id);
        return articulo;
    }

    public List<ArticuloListResponseDTO> buscarPorNombre(String titulo) {
        return articuloRepo.findAllByTitulo(titulo);
    }

    public List<ArticuloListResponseDTO> buscarArticulosPorPropietario(Long id) {
        Usuario usuario = usuarioService.buscarUsuarioEntity(id);
        return articuloRepo.findAllByPropietario(id);
    }
}
