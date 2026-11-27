package br.com.biblioteca.infrastructure.persistence.repository;

import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.repository.UsuarioRepository;
import br.com.biblioteca.infrastructure.persistence.entity.UsuarioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpa;

    @Override
    public Usuario salvar(Usuario usuario) {
        return paraDominio(jpa.save(paraEntity(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpa.findById(id).map(this::paraDominio);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpa.findByEmail(email).map(this::paraDominio);
    }

    @Override
    public void excluir(Long id) {
        jpa.deleteById(id);
    }

    private UsuarioJpaEntity paraEntity(Usuario usuario) {
        return new UsuarioJpaEntity(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil());
    }

    private Usuario paraDominio(UsuarioJpaEntity usuario) {
        return new Usuario(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfil());
    }
}
