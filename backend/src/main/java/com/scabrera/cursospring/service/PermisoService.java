package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.PermisoDTO;
import com.scabrera.cursospring.mapper.PermisoMapper;
import com.scabrera.cursospring.models.Permiso;
import com.scabrera.cursospring.repository.PermisoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermisoService {

    private final PermisoRepository permisoRepository;
    private final PermisoMapper permisoMapper;

    public PermisoService(PermisoRepository permisoRepository, PermisoMapper permisoMapper) {
        this.permisoRepository = permisoRepository;
        this.permisoMapper = permisoMapper;
    }

    /* Servicios que devuelven DTOs */
    public List<PermisoDTO> traerPermisosDTO() {
        return permisoRepository.findAll()
                .stream()
                .map(permisoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /* Servicios que devuelven entidades */
    public List<Permiso> traerPermisosEntidad() {
        return permisoRepository.findAll();
    }

    public Permiso buscarPermisoPorNombreEntidad(String nombre) {
        return permisoRepository.findByNombre(nombre)
            .orElseThrow(() -> new RuntimeException("Permiso no Encontrado con nombre: " + nombre));
    }
}
