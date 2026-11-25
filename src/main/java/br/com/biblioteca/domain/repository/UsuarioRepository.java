package br.com.biblioteca.domain.repository;

import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    void excluir(Long id);
}
