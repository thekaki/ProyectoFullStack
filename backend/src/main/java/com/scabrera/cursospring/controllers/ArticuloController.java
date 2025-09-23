package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.ArticuloDetailResponseDTO;
import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.mapper.ArticuloMapper;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticuloController {

    private ArticuloService articuloService;
    private ArticuloMapper articuloMapper;

    public ArticuloController(ArticuloService articuloService, ArticuloMapper articuloMapper) {
        this.articuloService = articuloService;
        this.articuloMapper = articuloMapper;
    }

    @GetMapping("/articulos")
    public List<ArticuloListResponseDTO> traerArticulos() {
        return articuloService.traerArticulos();
    }

    @GetMapping("/articulo/{id}")
    public ArticuloDetailResponseDTO traerArticulos(@PathVariable Long id) {
        Articulo articulo = articuloService.buscarId(id);
        return articuloMapper.toDetailDTO(articulo);
    }

}
