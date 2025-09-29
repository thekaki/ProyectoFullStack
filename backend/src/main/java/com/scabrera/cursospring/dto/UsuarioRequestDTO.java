package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String password;
}