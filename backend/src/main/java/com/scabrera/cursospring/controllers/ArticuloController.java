package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @GetMapping("/articulos")
    public List<Articulo> traerArticulos() {
        return articuloService.traerArticulos();
    }

}
