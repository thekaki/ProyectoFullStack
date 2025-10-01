package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.ArticuloRepository;
import com.scabrera.cursospring.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepo;
    private final UsuarioService usuarioService;
    private final CurrentUserService currentUserService;

    public ArticuloService(ArticuloRepository articuloRepo, UsuarioService usuarioService, CurrentUserService currentUserService) {
        this.articuloRepo = articuloRepo;
        this.usuarioService = usuarioService;
        this.currentUserService = currentUserService;
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

        articulo.setPropietario(user);
        return articuloRepo.save(articulo);
    }

    public Articulo eliminarArticulo(Long id) {
        Articulo articulo = buscarId(id);
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
