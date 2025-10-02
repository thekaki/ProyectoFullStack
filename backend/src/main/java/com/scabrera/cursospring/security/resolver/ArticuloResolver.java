package com.scabrera.cursospring.security.resolver;

import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.repository.ArticuloRepository;
import org.springframework.stereotype.Component;

@Component
public class ArticuloResolver implements EntityResolver {

    private final ArticuloRepository articuloRepository;

    public ArticuloResolver(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }

    @Override
    public Articulo resolve(Long id) {
        return articuloRepository.findById(id).orElse(null);
    }

    @Override
    public String getEntityName() {
        return "Articulo"; // Debe coincidir con lo que usas en @PreAuthorize
    }
}
