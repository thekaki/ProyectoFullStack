package com.scabrera.cursospring.dto;

import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.Comentario;
import com.scabrera.cursospring.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioResponseDTO {
    private Long id;
    private Long userId;
    private Long articuloId;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private Long comentarioPadreId;
    private List<ComentarioResponseDTO> respuestas;
}
