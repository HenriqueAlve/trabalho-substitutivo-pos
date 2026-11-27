package br.com.biblioteca.application.usecase;

import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.LivroRepository;

public class ExcluirLivroUseCase {

    private final LivroRepository livroRepository;
    public ExcluirLivroUseCase(LivroRepository livroRepository) {
        this.livroRepository = livroRepository; }

    public void executar(Long id) {
        livroRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
        livroRepository.excluir(id);
    }
}
