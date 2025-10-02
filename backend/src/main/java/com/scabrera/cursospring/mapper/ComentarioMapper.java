package com.scabrera.cursospring.mapper;

import com.scabrera.cursospring.dto.ComentarioRequestDTO;
import com.scabrera.cursospring.dto.ComentarioResponseDTO;
import com.scabrera.cursospring.models.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {
    Comentario RequestDTOToEntity(ComentarioRequestDTO comentarioDTO);

    @Mapping(target = "articuloId", source = "articulo.id")
    @Mapping(target = "userId", source = "autor.id")
    @Mapping(target = "comentarioPadreId", source = "comentarioPadre.id")
    @Mapping(target = "respuestas", source = "respuestas")
    ComentarioResponseDTO EntityToResponseDTO(Comentario comentario);
}
