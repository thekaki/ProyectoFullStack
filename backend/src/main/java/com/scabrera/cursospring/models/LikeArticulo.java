package com.scabrera.cursospring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes_articulo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "articulo_id"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeArticulo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    private LocalDateTime fechaLike;
}
