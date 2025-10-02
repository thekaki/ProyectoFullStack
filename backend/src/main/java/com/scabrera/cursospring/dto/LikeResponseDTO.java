package com.scabrera.cursospring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeResponseDTO {
    private Boolean usuarioHaLikeado;
    private Long totalLikes;

    public LikeResponseDTO(Long totalLikes) {
        this.totalLikes = totalLikes;
    }

    public LikeResponseDTO(Boolean usuarioHaLikeado) {
        this.usuarioHaLikeado = usuarioHaLikeado;
    }
}
