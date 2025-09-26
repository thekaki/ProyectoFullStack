package com.scabrera.cursospring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloRequestDTO {
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    private String descripcion;
    private String contenido;
    private String imagen;
}
