package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.ApiResponseDTO;
import com.scabrera.cursospring.dto.ArticuloListResponseDTO;
import com.scabrera.cursospring.dto.UsuarioRequestDTO;
import com.scabrera.cursospring.dto.UsuarioResponseDTO;
import com.scabrera.cursospring.mapper.UsuarioMapper;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.service.ArticuloService;
import com.scabrera.cursospring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ArticuloService articuloService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, ArticuloService articuloService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.articuloService = articuloService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/usuarios")
    public List<UsuarioResponseDTO> traerUsuarios(){
        return usuarioService.traerUsuariosDTO();
    }

    @GetMapping("/usuarios/{id}")
    public UsuarioResponseDTO buscarUsuario(@PathVariable Long id){
        return usuarioService.buscarUsuarioDTO(id);
    }

    @PostMapping("/usuarios")
    public UsuarioResponseDTO crearUsuario(@RequestBody UsuarioRequestDTO user){
        return usuarioService.crearUsuario(user);
    }

    @PutMapping("/usuarios/{id}")
    public UsuarioResponseDTO editarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO user){
        return usuarioService.editarUsuario(id, user);
    }

    @DeleteMapping("/usuarios/{id}")
    public void eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
    }

    @GetMapping("/usuarios/{id}/articulos")
    public ResponseEntity<ApiResponseDTO<List<ArticuloListResponseDTO>>> buscarArticulosPorPropietario(@PathVariable Long id) {
        List<ArticuloListResponseDTO> listaArticulos = articuloService.buscarArticulosPorPropietario(id);
        return ResponseEntity.ok(ApiResponseDTO.success(listaArticulos, "Artículos encontrados con éxito"));
    }

    @GetMapping("usuarios/me")
    public UsuarioResponseDTO me(Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.buscarUsuarioByEmailDTO(email);
    }
}
