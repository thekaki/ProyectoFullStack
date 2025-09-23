package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloDetailResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String imagen;
}
