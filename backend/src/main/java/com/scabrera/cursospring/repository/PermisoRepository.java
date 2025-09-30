package com.scabrera.cursospring.repository;

import com.scabrera.cursospring.models.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<List<Permiso>> findByNombre(String nombre);
}
