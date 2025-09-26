package com.scabrera.cursospring.repository;

import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.models.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    @Query("SELECT new com.scabrera.cursospring.dto.ArticuloListResponseDTO(a.id, a.titulo, a.descripcion, a.imagen) " +
            "FROM Articulo a")
    List<ArticuloListResponseDTO> findAllWithoutContenido();

    @Query("SELECT new com.scabrera.cursospring.dto.ArticuloListResponseDTO(a.id, a.titulo, a.descripcion, a.imagen) " +
            "FROM Articulo a WHERE LOWER(a.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<ArticuloListResponseDTO> findAllByTitulo(String titulo);

}
