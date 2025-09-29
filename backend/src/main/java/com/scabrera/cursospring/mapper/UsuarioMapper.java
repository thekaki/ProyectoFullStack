package com.scabrera.cursospring.mapper;

import com.scabrera.cursospring.dto.UsuarioRequestDTO;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.dto.UsuarioResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponseDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioRequestDTO dto);
}
