package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.ApiResponseDTO;
import com.scabrera.cursospring.dto.ArticuloDetailResponseDTO;
import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.dto.ArticuloRequestDTO;
import com.scabrera.cursospring.mapper.ArticuloMapper;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.service.ArticuloService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;
    private final ArticuloMapper articuloMapper;

    public ArticuloController(ArticuloService articuloService, ArticuloMapper articuloMapper) {
        this.articuloService = articuloService;
        this.articuloMapper = articuloMapper;
    }

    @GetMapping()
    public ResponseEntity<ApiResponseDTO<List<ArticuloListResponseDTO>>> traerArticulos() {
       List<ArticuloListResponseDTO> listaArticulos = articuloService.traerArticulos();
       return ResponseEntity.ok(ApiResponseDTO.success(listaArticulos, "Artículos econtrados con exito"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ArticuloDetailResponseDTO>> buscarArticulo(@PathVariable Long id) {
        Articulo articulo = articuloService.buscarId(id);
        ArticuloDetailResponseDTO respuesta = articuloMapper.toDetailDTO(articulo);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Articulo encontrado con exito"));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ArticuloListResponseDTO>>> buscarPorTitulo(@RequestParam String titulo) {
        List<ArticuloListResponseDTO> listaArticulos = articuloService.buscarPorNombre(titulo);
        return ResponseEntity.ok(ApiResponseDTO.success(listaArticulos, "Articulos econtrados con éxito"));
    }

    @PostMapping()
    public ResponseEntity<ApiResponseDTO<ArticuloDetailResponseDTO>> crearArticulo(@Valid @RequestBody ArticuloRequestDTO articuloRequestDTO){
        Articulo articulo = articuloMapper.toEntity(articuloRequestDTO);
        Articulo articuloCreado = articuloService.crearArticulo(articulo);
        ArticuloDetailResponseDTO respuesta = articuloMapper.toDetailDTO(articuloCreado);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Artículo creado con éxito"));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ArticuloDetailResponseDTO>> eliminarArticulo(@PathVariable Long id) {
        Articulo articuloEliminado = articuloService.eliminarArticulo(id);
        ArticuloDetailResponseDTO articuloEliniadoDTO = articuloMapper.toDetailDTO(articuloEliminado);
        return ResponseEntity.ok(ApiResponseDTO.success(articuloEliniadoDTO, "Articulo eliminado con exito"));
    }

}
