package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.ApiResponseDTO;
import com.scabrera.cursospring.dto.ArticuloDetailResponseDTO;
import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.dto.ArticuloRequestDTO;
import com.scabrera.cursospring.mapper.ArticuloMapper;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.service.ArticuloService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/articulo")
    public ResponseEntity<ApiResponseDTO<ArticuloDetailResponseDTO>> crearArticulo(@Valid @RequestBody ArticuloRequestDTO articuloRequestDTO){
        Articulo articulo = articuloMapper.toEntity(articuloRequestDTO);
        Articulo articuloCreado = articuloService.crearArticulo(articulo);
        ArticuloDetailResponseDTO respuesta = articuloMapper.toDetailDTO(articuloCreado);

        ApiResponseDTO<ArticuloDetailResponseDTO> apiResponse =
                new ApiResponseDTO<>(respuesta, "Artículo creado con éxito");

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

    }

}
