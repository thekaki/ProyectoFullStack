package com.scabrera.cursospring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponseDTO (

    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("refresh_token")
    String refreshToken
) {
}