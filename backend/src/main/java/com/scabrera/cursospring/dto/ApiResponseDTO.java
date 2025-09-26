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

    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }
}
