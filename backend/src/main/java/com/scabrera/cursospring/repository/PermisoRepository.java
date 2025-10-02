package com.scabrera.cursospring.repository;

import com.scabrera.cursospring.models.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    @Query("SELECT a FROM Permiso a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Permiso> findByNombre(String nombre);
}
