package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.repository.ArticuloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {

    private final ArticuloRepository articuloRepo;

    public ArticuloService(ArticuloRepository articuloRepo) {
        this.articuloRepo = articuloRepo;
    }

    public List<ArticuloListResponseDTO> traerArticulos() {
        return articuloRepo.findAllWithoutContenido();
    }

    public Articulo buscarId(Long id) {
        return articuloRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Artículo no Encontrado"));
    }

    public Articulo crearArticulo(Articulo articulo) {
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
}
