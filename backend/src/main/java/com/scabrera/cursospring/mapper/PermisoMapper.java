package com.scabrera.cursospring.mapper;

import com.scabrera.cursospring.dto.PermisoDTO;
import com.scabrera.cursospring.models.Permiso;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermisoMapper {

    Permiso toEntity(PermisoDTO permisoDTO);
    PermisoDTO toDTO(Permiso permiso);

}
