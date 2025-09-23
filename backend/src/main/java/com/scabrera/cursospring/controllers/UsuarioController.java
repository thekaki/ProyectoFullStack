package com.scabrera.cursospring.controllers;

import com.scabrera.cursospring.dto.UsuarioCreateDTO;
import com.scabrera.cursospring.dto.UsuarioResponseDTO;
import com.scabrera.cursospring.mapper.UsuarioMapper;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @GetMapping("/usuarios")
    public List<UsuarioResponseDTO> traerUsuarios(){
        return usuarioService.traerUsuarios()
                .stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/usuario/{id}")
    public UsuarioResponseDTO buscarUsuario(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarUsuario(id);
        return usuarioMapper.toDTO(usuario);
    }

    @PostMapping("/usuario")
    public UsuarioResponseDTO crearUsuario(@RequestBody UsuarioCreateDTO userCreateDTO){
        System.out.println(userCreateDTO);
        Usuario usuario = usuarioMapper.toEntity(userCreateDTO);

        Usuario actualizado = usuarioService.crearUsuario(usuario);

        return usuarioMapper.toDTO(actualizado);
    }

    @PutMapping("/usuario/{id}")
    public UsuarioResponseDTO editarUsuario(@PathVariable Long id, @RequestBody UsuarioCreateDTO userCreateDTO){
        Usuario usuario = usuarioMapper.toEntity(userCreateDTO);
        Usuario actualizado = usuarioService.editarUsuario(id, usuario);
        return usuarioMapper.toDTO(actualizado);
    }

    @DeleteMapping("/usuario/{id}")
    public void eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
    }

    @GetMapping("/me")
    public UsuarioResponseDTO me(Authentication authentication) {
        String email = authentication.getName(); // viene del JWT
        Usuario usuario = usuarioService.buscarUsuarioByEmail(email);

        return usuarioMapper.toDTO(usuario);
    }
}
