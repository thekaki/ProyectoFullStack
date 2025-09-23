package com.scabrera.cursospring.service;

import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.repository.ArticuloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {

    private ArticuloRepository articuloRepo;

    public ArticuloService(ArticuloRepository articuloRepo) {
        this.articuloRepo = articuloRepo;
    }

    public List<Articulo> traerArticulos() {
        return articuloRepo.findAll();
    }
}
