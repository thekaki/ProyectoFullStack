package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.ApiResponseDTO;
import com.scabrera.cursospring.dto.PermisoDTO;
import com.scabrera.cursospring.mapper.PermisoMapper;
import com.scabrera.cursospring.models.Permiso;
import com.scabrera.cursospring.repository.PermisoRepository;
import com.scabrera.cursospring.security.AuthorizationService;
import com.scabrera.cursospring.security.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermisoService {

    private final PermisoRepository permisoRepository;
    private final PermisoMapper permisoMapper;
    private final CurrentUserService currentUserService;
    private final AuthorizationService authorizationService;
    private final UsuarioService usuarioService;


    public PermisoService(PermisoRepository permisoRepository, PermisoMapper permisoMapper, CurrentUserService currentUserService, AuthorizationService authorizationService, UsuarioService usuarioService) {
        this.permisoRepository = permisoRepository;
        this.permisoMapper = permisoMapper;
        this.currentUserService = currentUserService;
        this.authorizationService = authorizationService;
        this.usuarioService = usuarioService;
    }

    /* Servicios que devuelven DTOs */
    public List<PermisoDTO> traerPermisosDTO() {
        return permisoRepository.findAll()
                .stream()
                .map(permisoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ApiResponseDTO<List<PermisoDTO>>> buscarPermisoPorNombreDTO(@RequestParam String nombre){
        List<Permiso> listaPermisoBBDD = permisoRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Permiso no Encontrado con nombre: " + nombre));
        List<PermisoDTO> respuesta = permisoMapper.toListDTO(listaPermisoBBDD);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Permiso creado con éxito"));
    }

    public ResponseEntity<ApiResponseDTO<PermisoDTO>> crearPermisoDTO(PermisoDTO permisoDTO) {
        Permiso permiso = permisoMapper.toEntity(permisoDTO);
        List<Permiso> listaPermisosBBDD = buscarPermisoPorNombreEntidad("PERMISO_CREATE");
        authorizationService.checkPermission(listaPermisosBBDD);

        Permiso guardado = permisoRepository.save(permiso);
        PermisoDTO respuesta = permisoMapper.toDTO(guardado);

        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Permiso creado con éxito"));

    }

    /* Servicios que devuelven entidades */
    public List<Permiso> traerPermisosEntidad() {
        return permisoRepository.findAll();
    }

    public List<Permiso> buscarPermisoPorNombreEntidad(String nombre) {
        return permisoRepository.findByNombre(nombre)
            .orElseThrow(() -> new RuntimeException("Permiso no Encontrado con nombre: " + nombre));
    }
}
