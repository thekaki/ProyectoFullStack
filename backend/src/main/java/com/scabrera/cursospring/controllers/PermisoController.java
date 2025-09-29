package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.ApiResponseDTO;
import com.scabrera.cursospring.dto.PermisoDTO;
import com.scabrera.cursospring.service.PermisoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
