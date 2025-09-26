package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    private boolean success;
    private String message;
    private Map<String, String> errors; // para errores de validación, opcional

    // Constructor simple para errores sin "errors"
    public ErrorResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
