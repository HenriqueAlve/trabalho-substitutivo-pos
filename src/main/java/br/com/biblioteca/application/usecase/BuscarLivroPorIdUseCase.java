package br.com.biblioteca.application.usecase;

import br.com.biblioteca.application.dto.LivroResponse;
import br.com.biblioteca.domain.entity.Livro;
import br.com.biblioteca.domain.exception.RecursoNaoEncontradoException;
import br.com.biblioteca.domain.repository.LivroRepository;

public class BuscarLivroPorIdUseCase {

    private final LivroRepository repository;

    public BuscarLivroPorIdUseCase(LivroRepository repository) {
        this.repository = repository;
    }

    public LivroResponse executar(Long id) {
        Livro livro = repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado: " + id));
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.isDisponivel());
    }
}
