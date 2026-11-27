package br.com.biblioteca.application.usecase;


import br.com.biblioteca.application.dto.AtualizarUsuarioRequest;
import br.com.biblioteca.application.dto.UsuarioResponse;
import br.com.biblioteca.domain.entity.Usuario;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.UsuarioRepository;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public AtualizarUsuarioUseCase(UsuarioRepository repository) {
        this.usuarioRepository = repository;
    }

    public UsuarioResponse executar(Long id, AtualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado: " + id));
        usuario.atualizar(request.nome(), request.email());
        Usuario salvo = usuarioRepository.salvar(usuario);
        return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getPerfil());
    }
}
