package com.scabrera.cursospring.repository;

import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByArticuloOrderByFechaCreacionAsc(Articulo articulo);
}

