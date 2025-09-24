package com.scabrera.cursospring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;

    // Constructor rápido para éxito
    public ApiResponseDTO(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
    }

    // Constructor rápido para error
    public ApiResponseDTO(String message) {
        this.success = false;
        this.message = message;
        this.data = null;
    }
}
