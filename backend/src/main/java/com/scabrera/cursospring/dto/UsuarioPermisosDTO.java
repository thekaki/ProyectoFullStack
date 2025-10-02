package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UsuarioPermisosDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    private List<RolDTO> roles;
}