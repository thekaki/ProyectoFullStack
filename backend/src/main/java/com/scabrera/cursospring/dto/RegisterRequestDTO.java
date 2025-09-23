package com.scabrera.cursospring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequestDTO (

    String email,
    String password,
    String name

) {
}