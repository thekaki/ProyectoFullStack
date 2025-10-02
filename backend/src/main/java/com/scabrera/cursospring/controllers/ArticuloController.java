package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.*;
import com.scabrera.cursospring.mapper.ArticuloMapper;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.service.ArticuloService;
import com.scabrera.cursospring.service.ComentarioService;
import com.scabrera.cursospring.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;
    private final ArticuloMapper articuloMapper;
    private final LikeService likeService;
    private final ComentarioService comentarioService;

    public ArticuloController(ArticuloService articuloService, ArticuloMapper articuloMapper, LikeService likeService, ComentarioService comentarioService) {
        this.articuloService = articuloService;
        this.articuloMapper = articuloMapper;
        this.likeService = likeService;
        this.comentarioService = comentarioService;
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
    @PreAuthorize("hasPermission(null, 'ARTICULO_CREATE')")
    public ResponseEntity<ApiResponseDTO<ArticuloDetailResponseDTO>> crearArticulo(@Valid @RequestBody ArticuloRequestDTO articuloRequestDTO){
        Articulo articulo = articuloMapper.toEntity(articuloRequestDTO);
        Articulo articuloCreado = articuloService.crearArticulo(articulo);
        ArticuloDetailResponseDTO respuesta = articuloMapper.toDetailDTO(articuloCreado);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Artículo creado con éxito"));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Articulo', 'ARTICULO_DELETE')")
    public ResponseEntity<ApiResponseDTO<ArticuloDetailResponseDTO>> eliminarArticulo(@PathVariable Long id) {
        Articulo articuloEliminado = articuloService.eliminarArticulo(id);
        ArticuloDetailResponseDTO articuloEliniadoDTO = articuloMapper.toDetailDTO(articuloEliminado);
        return ResponseEntity.ok(ApiResponseDTO.success(articuloEliniadoDTO, "Articulo eliminado con exito"));
    }

    //Gestion de likes
    @PostMapping("/{idArticulo}/likes")
    public ResponseEntity<ApiResponseDTO<String>> darLike(@PathVariable Long idArticulo) {
        likeService.darLike(idArticulo);
        return ResponseEntity.ok(ApiResponseDTO.success("Se ha dado like exitosamente"));
    }

    @DeleteMapping("/{idArticulo}/likes")
    public ResponseEntity<ApiResponseDTO<String>> quitarLike(@PathVariable Long idArticulo) {
        likeService.quitarLike(idArticulo);
        return ResponseEntity.ok(ApiResponseDTO.success("Like borrado exitosamente"));
    }

    @GetMapping("/{idArticulo}/likes")
    public ResponseEntity<ApiResponseDTO<LikeResponseDTO>> obtenerLikesArticulo(@PathVariable Long idArticulo) {
        long totalLikes = likeService.contarLikesArticulo(idArticulo);

        LikeResponseDTO respuesta = new LikeResponseDTO(totalLikes);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Información de likes obtenida con éxito"));
    }

    @GetMapping("/{idArticulo}/likes/buscar")
    public ResponseEntity<ApiResponseDTO<LikeResponseDTO>> confirmarLikeUsuario(@PathVariable Long idArticulo,
                                                                                @RequestParam Long usuarioId) {
        boolean haLikeado = likeService.usuarioHaLikeado(usuarioId, idArticulo);
        long totalLikes = likeService.contarLikesArticulo(idArticulo);

        LikeResponseDTO respuesta = new LikeResponseDTO(haLikeado, totalLikes);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Información de likes obtenida con éxito"));
    }

    //Gestion de comentarios

    @PostMapping("/{idArticulo}/comentarios")
    public ResponseEntity<ApiResponseDTO<ComentarioResponseDTO>> comentar(@PathVariable Long idArticulo,
                                                                         @RequestBody ComentarioRequestDTO comentarioDTO) {
        ComentarioResponseDTO creado = comentarioService.crearComentario(idArticulo, comentarioDTO);
        return ResponseEntity.ok(ApiResponseDTO.success(creado, "Comentario creado con éxito"));
    }

    @GetMapping("/{idArticulo}/comentarios")
    public ResponseEntity<ApiResponseDTO<List<ComentarioResponseDTO>>> getComentarios(@PathVariable Long idArticulo) {
        List<ComentarioResponseDTO> lista = comentarioService.listarComentarios(idArticulo);
        return ResponseEntity.ok(ApiResponseDTO.success(lista, "Comentarios obtenidos con éxito"));
    }

}
