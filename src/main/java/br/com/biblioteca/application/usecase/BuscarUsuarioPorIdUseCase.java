package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.UsuarioResponse;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.UsuarioRepository;

public class BuscarUsuarioPorIdUseCase {

    private final UsuarioRepository usuarioRepository;

    public BuscarUsuarioPorIdUseCase(UsuarioRepository repository){
        this.usuarioRepository = repository;
    }

    public UsuarioResponse executar(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado: " + id));
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
