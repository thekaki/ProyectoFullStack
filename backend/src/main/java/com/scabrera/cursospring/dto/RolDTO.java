package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RolDTO {
    private Long id;
    private String nombre;
    private List<String> permisos;
}
