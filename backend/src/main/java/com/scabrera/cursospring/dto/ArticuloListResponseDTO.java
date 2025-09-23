package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloListResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
}
