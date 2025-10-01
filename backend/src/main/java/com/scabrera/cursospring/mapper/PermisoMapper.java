package com.scabrera.cursospring.mapper;

import com.scabrera.cursospring.dto.PermisoDTO;
import com.scabrera.cursospring.models.Permiso;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermisoMapper {

    Permiso toEntity(PermisoDTO permisoDTO);
    PermisoDTO toDTO(Permiso permiso);
    List<PermisoDTO> toListDTO(List<Permiso> listaPermisos);

}
