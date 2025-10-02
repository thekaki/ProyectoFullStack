package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioRequestDTO {
    private String contenido;
    private Long comentarioPadreId;
}
