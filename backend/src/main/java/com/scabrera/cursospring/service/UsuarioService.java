package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.RolDTO;
import com.scabrera.cursospring.dto.UsuarioPermisosDTO;
import com.scabrera.cursospring.dto.UsuarioRequestDTO;
import com.scabrera.cursospring.dto.UsuarioResponseDTO;
import com.scabrera.cursospring.mapper.UsuarioMapper;
import com.scabrera.cursospring.models.Permiso;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ======= Métodos para Controller (devuelven DTO) =======

    public List<UsuarioResponseDTO> traerUsuariosDTO(){
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarUsuarioDTO(Long id){
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
    }

    public UsuarioResponseDTO buscarUsuarioByEmailDTO(String email){
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado con email: " + email));
    }

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO user) {
        usuarioRepository.findByEmail(user.getEmail())
                .ifPresent(u -> { throw new RuntimeException("El email ya está registrado"); });

        Usuario nuevo = usuarioMapper.toEntity(user);
        nuevo.setPassword(passwordEncoder.encode(user.getPassword()));
        Usuario guardado = usuarioRepository.save(nuevo);
        return usuarioMapper.toDTO(guardado);
    }

    public UsuarioResponseDTO editarUsuario(Long id, UsuarioRequestDTO user) {
        Usuario existente = buscarUsuarioEntity(id);
        existente.setNombre(user.getNombre());
        existente.setApellido(user.getApellido());
        existente.setEmail(user.getEmail());
        existente.setTelefono(user.getTelefono());
        if (user.getPassword() != null) {
            existente.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        Usuario guardado = usuarioRepository.save(existente);
        return usuarioMapper.toDTO(guardado);
    }

    public UsuarioPermisosDTO obtenerPermisosUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<RolDTO> rolesDTO = usuario.getRoles().stream()
                .map(rol -> new RolDTO(
                        rol.getId(),
                        rol.getNombre(),
                        rol.getPermisos().stream()
                                .map(Permiso::getNombre)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        UsuarioPermisosDTO respuesta = new UsuarioPermisosDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                rolesDTO
        );

        return respuesta;
    }

    // ======= Métodos internos (devuelven entidades) =======

    public Usuario buscarUsuarioEntity(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
