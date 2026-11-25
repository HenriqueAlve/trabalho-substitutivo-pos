package br.com.biblioteca.application.usecase;

import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.UsuarioRepository;

public class ExcluirUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public ExcluirUsuarioUseCase(UsuarioRepository repository) {
        this.usuarioRepository = repository; }

    public void executar(Long id) {
        usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario não encontrado: " + id));
        usuarioRepository.excluir(id);
    }
}
