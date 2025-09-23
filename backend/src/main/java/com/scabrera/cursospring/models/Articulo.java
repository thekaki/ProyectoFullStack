package com.scabrera.cursospring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articulos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(length = 500)
    private String descripcion;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(length = 2083)
    private String imagen;
}
