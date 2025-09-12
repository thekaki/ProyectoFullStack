package com.scabrera.cursospring.mapper;

import com.scabrera.cursospring.dto.UsuarioCreateDTO;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.dto.UsuarioResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponseDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioCreateDTO dto);
}
