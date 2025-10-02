package com.scabrera.cursospring.repository;

import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.LikeArticulo;
import com.scabrera.cursospring.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeArticuloRepository extends JpaRepository<LikeArticulo, Long> {

    boolean existsByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    long countByArticulo(Articulo articulo);

    Optional<LikeArticulo> findByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    List<LikeArticulo> findByUsuario(Usuario usuario);
}

