package br.com.biblioteca.application.usecase;

import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.LivroRepository;

public class ExcluirLivroUseCase {

    private final LivroRepository repository;
    public ExcluirLivroUseCase(LivroRepository repository) { this.repository = repository; }

    public void executar(Long id) {
        repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
        repository.excluir(id);
    }
}
