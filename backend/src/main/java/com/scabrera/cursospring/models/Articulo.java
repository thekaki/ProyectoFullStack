package com.scabrera.cursospring.models;

import com.scabrera.cursospring.security.Ownable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articulos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Articulo implements Ownable {

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario propietario;

    @Override
    public Long getOwnerId() {
        return propietario != null ? propietario.getId() : null;
    }
}
