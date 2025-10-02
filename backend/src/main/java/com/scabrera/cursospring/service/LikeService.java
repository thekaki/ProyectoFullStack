package com.scabrera.cursospring.service;

import com.scabrera.cursospring.dto.ArticuloDetailResponseDTO;
import com.scabrera.cursospring.mapper.ArticuloMapper;
import com.scabrera.cursospring.models.Articulo;
import com.scabrera.cursospring.models.LikeArticulo;
import com.scabrera.cursospring.models.Usuario;
import com.scabrera.cursospring.repository.ArticuloRepository;
import com.scabrera.cursospring.repository.LikeArticuloRepository;
import com.scabrera.cursospring.repository.UsuarioRepository;
import com.scabrera.cursospring.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private final LikeArticuloRepository likeRepo;
    private final ArticuloRepository articuloRepo;
    private final UsuarioRepository usuarioRepo;
    private final ArticuloMapper articuloMapper;
    private final CurrentUserService currentUserService;

    public LikeService(LikeArticuloRepository likeRepo, ArticuloRepository articuloRepo, UsuarioRepository usuarioRepo, ArticuloMapper articuloMapper, CurrentUserService currentUserService) {
        this.likeRepo = likeRepo;
        this.articuloRepo = articuloRepo;
        this.usuarioRepo = usuarioRepo;
        this.articuloMapper = articuloMapper;
        this.currentUserService = currentUserService;
    }

    public void darLike(Long idArticulo) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));;
        Articulo art = articuloRepo.findById(idArticulo)
                .orElseThrow(() -> new RuntimeException("Artículo no Encontrado"));
        boolean existe = likeRepo.existsByUsuarioAndArticulo(user, art);
        if (!existe) {
            LikeArticulo like = new LikeArticulo();
            like.setUsuario(user);
            like.setArticulo(art);
            like.setFechaLike(LocalDateTime.now());
            likeRepo.save(like);
        } else {
            throw new RuntimeException("El usuario ya ha dado like al articulo");
        }
    }

    public void quitarLike(Long idArticulo) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Usuario user = usuarioRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
        Articulo art = articuloRepo.findById(idArticulo)
                .orElseThrow(() -> new RuntimeException("Artículo no Encontrado"));
        LikeArticulo existe = likeRepo.findByUsuarioAndArticulo(user, art)
                .orElseThrow(() -> new RuntimeException("Like no Encontrado"));

        likeRepo.delete(existe);
    }

    public long contarLikesArticulo(Long idArticulo) {
        Articulo art = articuloRepo.findById(idArticulo)
                .orElseThrow(() -> new RuntimeException("Artículo no Encontrado"));
        return likeRepo.countByArticulo(art);
    }

    public boolean usuarioHaLikeado(Long idUsuario, Long idArticulo) {
        Usuario user = usuarioRepo.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
        Articulo art = articuloRepo.findById(idArticulo).orElseThrow(() -> new RuntimeException("Artículo no Encontrado"));
        return likeRepo.existsByUsuarioAndArticulo(user, art);
    }

    public List<ArticuloDetailResponseDTO> articulosQueLeGustanUsuario(Long idUsuario) {
        Usuario user = usuarioRepo.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
        return likeRepo.findByUsuario(user)
                .stream()
                .map(LikeArticulo::getArticulo)
                .map(articuloMapper::toDetailDTO)
                .collect(Collectors.toList());
    }
}
