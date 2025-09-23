package com.scabrera.cursospring.service;

import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> traerUsuarios(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuario(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
    }

    public Usuario buscarUsuarioByEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado con email: " + email));
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario crearUsuario(Usuario user) {
        if (usuarioRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return usuarioRepository.save(user);
    }

    public Usuario editarUsuario(Long id, Usuario user) {
        Usuario existente = buscarUsuario(id);
        existente.setNombre(user.getNombre());
        existente.setApellido(user.getApellido());
        existente.setEmail(user.getEmail());
        existente.setTelefono(user.getTelefono());
        if (user.getPassword() != null) {
            existente.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return usuarioRepository.save(existente);
    }
}
