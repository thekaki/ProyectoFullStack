package com.scabrera.cursospring.mapper;

import com.scabrera.cursospring.dto.ArticuloDetailResponseDTO;
import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.dto.ArticuloRequestDTO;
import com.scabrera.cursospring.models.Articulo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticuloMapper {
    ArticuloListResponseDTO toListDTO(Articulo articulo);
    ArticuloDetailResponseDTO toDetailDTO(Articulo articulo);
    Articulo toEntity(ArticuloRequestDTO articuloRequestDTO);
}
