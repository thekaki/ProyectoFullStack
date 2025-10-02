package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.ApiResponseDTO;
import com.scabrera.cursospring.dto.PermisoDTO;
import com.scabrera.cursospring.service.PermisoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class PermisoController {

    private final PermisoService permisoService;

    public PermisoController(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

    @GetMapping("permisos")
    public ResponseEntity<ApiResponseDTO<List<PermisoDTO>>> traerPermisos() {
        List<PermisoDTO> listaPermisos = permisoService.traerPermisosDTO();
        return ResponseEntity.ok(ApiResponseDTO.success(listaPermisos, "Permisos listados con éxito"));
    }

    @GetMapping("permisos/buscar")
    public ResponseEntity<ApiResponseDTO<List<PermisoDTO>>> buscarPermisosPorNombre(@RequestParam String nombre) {
        List<PermisoDTO> listaPermisos = permisoService.buscarPermisoPorNombreDTO(nombre);
        return ResponseEntity.ok(ApiResponseDTO.success(listaPermisos, "Permisos listados con éxito"));
    }

    @PostMapping("permisos")
    @PreAuthorize("hasPermission(null, 'PERMISO_CREATE')")
    public ResponseEntity<ApiResponseDTO<PermisoDTO>> crearPermiso(@Valid @RequestBody PermisoDTO permisoRequest) {
        PermisoDTO respuesta = permisoService.crearPermisoDTO(permisoRequest);
        return ResponseEntity.ok(ApiResponseDTO.success(respuesta, "Permiso creado con éxito"));
    }
}
