package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.ComentarioRequestDTO;
import com.scabrera.cursospring.dto.ComentarioResponseDTO;
import com.scabrera.cursospring.mapper.ComentarioMapper;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.Comentario;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.ComentarioRepository;
import com.scabrera.cursospring.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ArticuloService articuloService;
    private final UsuarioService usuarioService;
    private final CurrentUserService currentUserService;
    private final ComentarioMapper comentarioMapper;

    public ComentarioService(ComentarioRepository comentarioRepository, ArticuloService articuloService, UsuarioService usuarioService, CurrentUserService currentUserService, ComentarioMapper comentarioMapper) {
        this.comentarioRepository = comentarioRepository;
        this.articuloService = articuloService;
        this.usuarioService = usuarioService;
        this.currentUserService = currentUserService;
        this.comentarioMapper = comentarioMapper;
    }

    public ComentarioResponseDTO crearComentario(Long idArticulo, ComentarioRequestDTO comentarioDTO) {
        Articulo articuloExistente = articuloService.buscarId(idArticulo);
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario usuarioExistente = usuarioService.buscarUsuarioEntity(currentUserId);

        Comentario guardarComentario = comentarioMapper.RequestDTOToEntity(comentarioDTO);

        guardarComentario.setArticulo(articuloExistente);
        guardarComentario.setAutor(usuarioExistente);

        if (comentarioDTO.getComentarioPadreId() != null) {
            Comentario padre = comentarioRepository.findById(comentarioDTO.getComentarioPadreId())
                    .orElseThrow(() -> new RuntimeException("Comentario padre no encontrado"));
            guardarComentario.setComentarioPadre(padre);
        }

        Comentario guardado = comentarioRepository.save(guardarComentario);
        return comentarioMapper.EntityToResponseDTO(guardado);
    }

    public List<ComentarioResponseDTO> listarComentarios(Long idArticulo) {
        Articulo articuloExistente = articuloService.buscarId(idArticulo);

        return comentarioRepository.findByArticuloOrderByFechaCreacionAsc(articuloExistente)
                .stream()
                .map(comentarioMapper::EntityToResponseDTO)
                .collect(Collectors.toList());
    }
}
